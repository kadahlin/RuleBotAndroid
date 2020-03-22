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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kyledahlin.myrulebot.rules.RuleEntry

typealias OnStringClick = (String) -> Unit

class EntryAdapter(private val onRuleClick: OnStringClick) :
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

        fun bind(entry: RuleEntry, onRuleClick: OnStringClick) {
            name.text = entry.ruleName
            itemView.setOnClickListener {
                onRuleClick(entry.activityName)
            }
        }

    }
}