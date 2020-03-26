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
import com.kyledahlin.myrulebot.backend.NameAndId
import com.kyledahlin.myrulebot.ui.OnNameIdSelection
import com.kyledahlin.reactions.databinding.MemberSelectionPageBinding

/**
 * Show the members for a particular guild
 */
internal class MemberSelectionPage : ReactionFragment(), OnNameIdSelection {

    private lateinit var _binding: MemberSelectionPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MemberSelectionPageBinding.inflate(inflater, container, false).apply {
            viewmodel = _viewModel
            lifecycleOwner = viewLifecycleOwner
            onMemberClick = this@MemberSelectionPage
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewModel.state.observe(viewLifecycleOwner, Observer(::displayState))
    }

    override fun onSelected(nameAndId: NameAndId) {
        findNavController().navigate(
            MemberSelectionPageDirections.actionMemberSelectionPageToEmojiSelectionPage(nameAndId)
        )
    }

    private fun displayState(state: ReactionState) {
        if (state is ReactionState.Loaded) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = state.guildName
        }
    }
}