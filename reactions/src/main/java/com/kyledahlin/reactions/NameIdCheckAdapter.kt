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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kyledahlin.myrulebot.backend.NameAndId

typealias OnNameIdChecked = (NameAndId, Boolean) -> Unit

class NameIdCheckAdapter :
    RecyclerView.Adapter<NameIdCheckAdapter.ViewHolder>() {

    private val _items = mutableListOf<NameAndId>()
    private val _checked = mutableSetOf<NameAndId>()

    private val _added = mutableSetOf<NameAndId>()
    private val _unadded = mutableSetOf<NameAndId>()

    fun setData(unadded: Collection<NameAndId>, added: Collection<NameAndId>) {
        _items.clear()
        _items.addAll(unadded)
        _items.addAll(added)
        _checked.addAll(added)

        _added.clear()
        _unadded.clear()

        notifyDataSetChanged()
    }

    fun getAddedRemoved(): Pair<Set<NameAndId>, Set<NameAndId>> {
        return _added to _unadded
    }

    class ViewHolder(itemView: View, val onNameIdClick: OnNameIdChecked) :
        RecyclerView.ViewHolder(itemView) {

        private val nameView = itemView.findViewById<TextView>(R.id.name_id_check_viewholder_name)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.name_id_check_viewholder_check)

        fun bind(nameAndId: NameAndId, isChecked: Boolean) {
            nameView.text = nameAndId.name
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = isChecked
            checkBox.setOnCheckedChangeListener { _, wasChecked ->
                onNameIdClick(nameAndId, wasChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.name_id_check_viewholder, parent, false)
        return ViewHolder(view) { nameId, isChecked ->
            val itemIndex = _items.indexOf(nameId)
            if (isChecked) {
                _unadded.remove(nameId)
                _added.add(nameId)
                _checked.add(nameId)
            } else {
                _added.remove(nameId)
                _unadded.add(nameId)
                _checked.remove(nameId)
            }
            notifyItemChanged(itemIndex)
        }
    }

    override fun getItemCount() = _items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isChecked = _checked.contains(_items[position])
        holder.bind(_items[position], isChecked)
    }
}