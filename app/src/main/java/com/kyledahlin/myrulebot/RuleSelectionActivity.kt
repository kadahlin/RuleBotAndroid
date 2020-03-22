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
package com.kyledahlin.myrulebot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyledahlin.myrulebot.rules.RuleClassViewModel
import com.kyledahlin.myrulebot.rules.RuleEntry
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class RuleSelectionActivity : AppCompatActivity() {

    @Inject
    lateinit var _factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        coreComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rule_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = EntryAdapter(::launchActivity)
        }
        val viewModel = ViewModelProvider(this, _factory)[RuleClassViewModel::class.java]
        viewModel.ruleEntries.observe(this, Observer(::updateEntries))
        viewModel.refreshRules()
    }

    private fun updateEntries(entries: List<RuleEntry>) {
        (rule_recycler.adapter as EntryAdapter).setData(entries)
    }

    private fun launchActivity(activityName: String) {
        val intent = Intent(this, Class.forName(activityName))
        startActivity(intent)
    }
}
