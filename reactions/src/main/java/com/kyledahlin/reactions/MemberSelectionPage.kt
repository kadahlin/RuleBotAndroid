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
import kotlinx.android.synthetic.main.member_selection_page.*

/**
 * Show the members for a particular guild
 */
internal class MemberSelectionPage : ReactionFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.member_selection_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reaction_member_recycler.setOnClick {
            findNavController().navigate(
                MemberSelectionPageDirections.actionMemberSelectionPageToEmojiSelectionPage(
                    it.id,
                    it.name
                )
            )
        }
        _viewModel.state.observe(viewLifecycleOwner, Observer(::displayState))
    }

    private fun displayState(state: ReactionState) {
        if (state is ReactionState.Loaded) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = state.guildName
            reaction_member_recycler.setData(state.members)
        }
    }
}