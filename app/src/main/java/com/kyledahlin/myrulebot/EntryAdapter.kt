package com.kyledahlin.myrulebot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kyledahlin.myrulebot.rules.RuleEntry

typealias OnRuleClick = (String) -> Unit

class EntryAdapter(private val onRuleClick: OnRuleClick) :
    RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    private val _entries = ArrayList<RuleEntry>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.entry_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_entries[position], onRuleClick)
    }

    fun setData(entries: List<RuleEntry>) {
        _entries.clear()
        _entries.addAll(entries)
    }

    override fun getItemCount() = _entries.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.entry_view_holder_name)

        fun bind(entry: RuleEntry, onRuleClick: OnRuleClick) {
            name.text = entry.ruleName
            itemView.setOnClickListener {
                onRuleClick(entry.activityName)
            }
        }

    }
}