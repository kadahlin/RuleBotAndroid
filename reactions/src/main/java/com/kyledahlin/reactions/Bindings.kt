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

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kyledahlin.myrulebot.backend.NameAndId
import com.kyledahlin.myrulebot.ui.NameAndIdRecycler
import com.kyledahlin.myrulebot.ui.OnNameIdSelection

@BindingAdapter("emojis", "memberId")
internal fun bindEmojis(recycler: RecyclerView, state: ReactionState?, memberId: String?) {
    if (state is ReactionState.Loaded && memberId != null) {
        val emojis = state.emojis
        val addedIds = state.added.filter { it.memberId == memberId }.map { it.emojiId }
        val added = emojis.filter { addedIds.contains(it.id) }
        val unadded = emojis.filter { !addedIds.contains(it.id) }

        (recycler.adapter as NameIdCheckAdapter).setData(unadded, added)
    }
}

@BindingAdapter("guilds")
internal fun bindGuilds(recycler: NameAndIdRecycler, list: List<NameAndId>?) {
    if (list != null) recycler.setData(list)
}

@BindingAdapter("members")
internal fun bindMembers(recycler: NameAndIdRecycler, state: ReactionState?) {
    if (state is ReactionState.Loaded) {
        recycler.setData(state.members)
    }
}

@BindingAdapter("onSelection")
internal fun bindNameIdSelection(
    recycler: NameAndIdRecycler,
    onNameIdSelection: OnNameIdSelection
) {
    recycler.setOnClick(onNameIdSelection)
}