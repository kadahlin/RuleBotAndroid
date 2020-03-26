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
import androidx.navigation.fragment.findNavController
import com.kyledahlin.myrulebot.backend.NameAndId
import com.kyledahlin.myrulebot.ui.OnNameIdSelection
import com.kyledahlin.reactions.databinding.GuildSelectionPageBinding
import timber.log.Timber

internal class GuildSelectionPage : ReactionFragment(), OnNameIdSelection {

    private lateinit var _binding: GuildSelectionPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel.loadGuilds(useCache = true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GuildSelectionPageBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = _viewModel
            onGuildClick = this@GuildSelectionPage
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Guilds"
    }

    override fun onSelected(nameAndId: NameAndId) {
        Timber.d("loading guild $nameAndId")
        _viewModel.loadState(nameAndId.name, nameAndId.id, useCache = true)
        findNavController().navigate(GuildSelectionPageDirections.actionGuildSelectionPageToMemberSelectionPage())
    }
}