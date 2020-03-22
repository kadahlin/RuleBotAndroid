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
package com.kyledahlin.myrulebot.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyledahlin.myrulebot.R
import com.kyledahlin.myrulebot.backend.NameAndId

typealias OnNameIdClick = (NameAndId) -> Unit

class NameAndIdRecycler @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : RecyclerView(context, attrs, style) {
    init {
        layoutManager = LinearLayoutManager(context)
    }

    fun setOnClick(onNameIdClick: OnNameIdClick) {
        adapter = NameAndIdAdapter(onNameIdClick)
    }

    fun setData(namesAndIds: Collection<NameAndId>) {
        (adapter as? NameAndIdAdapter)?.setData(namesAndIds)
            ?: throw IllegalStateException("an adapter needs to be set before data is shown")
    }
}

class NameAndIdAdapter(private val onNameIdClick: OnNameIdClick) :
    RecyclerView.Adapter<NameAndIdAdapter.ViewHolder>() {

    private val _items = mutableListOf<NameAndId>()

    fun setData(collection: Collection<NameAndId>) {
        _items.clear()
        _items.addAll(collection)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, val onNameIdClick: OnNameIdClick) :
        RecyclerView.ViewHolder(itemView) {

        private val nameView = itemView.findViewById<TextView>(R.id.name_id_viewholder_text)

        fun bind(nameAndId: NameAndId) {
            nameView.text = nameAndId.name
            itemView.setOnClickListener { onNameIdClick(nameAndId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.name_id_viewholder, parent, false)
        return ViewHolder(view, onNameIdClick)
    }

    override fun getItemCount() = _items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_items[position])
    }
}