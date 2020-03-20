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
