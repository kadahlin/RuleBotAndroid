/*
*Copyright 2020 Kyle Dahlin
*
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
package com.kyledahlin.reactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyledahlin.myrulebot.backend.NameAndId
import com.kyledahlin.myrulebot.backend.RuleBotApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

internal abstract class ReactionViewModel : ViewModel() {

    abstract val guilds: LiveData<List<NameAndId>>

    abstract val state: LiveData<ReactionState>

    abstract fun postCommands(commands: Collection<Command>)

    abstract fun loadGuilds(useCache: Boolean = true)

    abstract fun loadState(guildName: String, guildId: Long, useCache: Boolean = true)
}

internal class ReactionViewModelImpl constructor(
    private val reactionService: ReactionService,
    private val ruleBotApi: RuleBotApi
) : ReactionViewModel() {

    init {
        Timber.d("created")
    }

    private val _state = MutableLiveData<ReactionState>().apply {
        value = ReactionState.Loading
    }
    private val _guilds = MutableLiveData<List<NameAndId>>()

    override val state: LiveData<ReactionState>
        get() = _state

    override val guilds: LiveData<List<NameAndId>>
        get() = _guilds

    override fun loadGuilds(useCache: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val guilds = ruleBotApi.getGuilds(useCache)
            _guilds.postValue(guilds)
        }
    }

    override fun postCommands(commands: Collection<Command>) {
        viewModelScope.launch(Dispatchers.IO) {
            commands.forEach {
                reactionService.postReactionCommand(it)
            }
            Timber.d("commands posted")
            val currentState = _state.value
            if (currentState is ReactionState.Loaded) {
                loadState(currentState.guildName, currentState.guildId)
            }
        }
    }

    override fun loadState(guildName: String, guildId: Long, useCache: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(ReactionState.Loading)
            try {
                val memberInfo = ruleBotApi.getMembers(guildId, useCache)
                val response = reactionService.list(guildId)
                val state = ReactionState.Loaded(
                    guildName = guildName,
                    guildId = guildId,
                    members = memberInfo,
                    emojis = response.emojis,
                    added = response.addedEmojis
                )
                _state.postValue(state)
            } catch (e: Exception) {
                Timber.e(e, "get error while trying to load state for guild id: [$guildId]")
                _state.postValue(ReactionState.Error("unknown error"))
            }
        }
    }
}

internal sealed class ReactionState {
    object Loading : ReactionState()
    data class Error(val message: String) : ReactionState()
    data class Loaded(
        val guildName: String,
        val guildId: Long,
        val members: List<NameAndId>,
        val emojis: List<NameAndId>,
        val added: List<AddedEmoji>
    ) : ReactionState()
}