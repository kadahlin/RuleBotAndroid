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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyledahlin.reactions.databinding.EmojiSelectionPageBinding
import kotlinx.android.synthetic.main.emoji_selection_page.*

internal class EmojiSelectionPage : ReactionFragment() {

    private var _guildId: String? = null
    private val args: EmojiSelectionPageArgs by navArgs()
    private lateinit var _binding: EmojiSelectionPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EmojiSelectionPageBinding.inflate(inflater, container, false).apply {
            viewmodel = _viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.memberNameId.name
        _binding.emojiSelectionRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = NameIdCheckAdapter()
        }
        _binding.emojiSelectionButton.setOnClickListener {
            val guildId = _guildId
            if (guildId != null) {
                val (added, unadded) = (emoji_selection_recycler.adapter as NameIdCheckAdapter).getAddedRemoved()
                val addCommands = added.map {
                    Command(
                        action = "add",
                        guildId = guildId,
                        emoji = it.id,
                        member = args.memberNameId.id
                    )
                }
                val removeCommands = unadded.map {
                    Command(
                        action = "remove",
                        guildId = guildId,
                        emoji = it.id,
                        member = args.memberNameId.id
                    )
                }
                _viewModel.postCommands(addCommands.union(removeCommands))
                findNavController().navigateUp()
            }
        }
        _viewModel.state.observe(viewLifecycleOwner, Observer(::loadState))
    }

    private fun loadState(state: ReactionState) {
        if (state is ReactionState.Loaded) {
            _guildId = state.guildId
        }
    }
}