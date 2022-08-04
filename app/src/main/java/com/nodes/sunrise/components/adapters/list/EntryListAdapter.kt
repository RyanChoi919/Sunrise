package com.nodes.sunrise.components.adapters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.comparators.EntryComparator
import com.nodes.sunrise.components.listeners.OnEntryClickListener
import com.nodes.sunrise.components.listeners.OnEntryLongClickListener
import com.nodes.sunrise.databinding.ListItemEntryBinding
import com.nodes.sunrise.db.entity.Entry

class EntryListAdapter : ListAdapter<Entry, EntryListAdapter.EntryViewHolder>(EntryComparator()) {

    lateinit var onEntryClickListener: OnEntryClickListener
    lateinit var onEntryLongClickListener: OnEntryLongClickListener

    init {
        setHasStableIds(true) // notifyDatasetChanged 가 호출될 때 RV가 깜빡이는 것을 방지
    }

    override fun getItemId(position: Int): Long {
        // notifyDatasetChanged 가 호출될 때 RV가 깜빡이는 것을 방지하기 위해 override 된 메소드
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val viewHolder = EntryViewHolder.create(parent)
        viewHolder.onClickListener = onEntryClickListener
        viewHolder.onLongClickListener = onEntryLongClickListener
        return viewHolder
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var _onClickListener: OnEntryClickListener? = null
        var onClickListener: OnEntryClickListener
            get() = _onClickListener!!
            set(value) {
                _onClickListener = value
                binding.root.setOnClickListener {
                    value.onClick(it, absoluteAdapterPosition, currentEntry)
                }
            }
        private var _onLongClickListener: OnEntryLongClickListener? = null
        var onLongClickListener: OnEntryLongClickListener
            get() = _onLongClickListener!!
            set(value) {
                _onLongClickListener = value
                binding.root.setOnLongClickListener {
                    value.onItemLongClick(it, absoluteAdapterPosition, currentEntry)
                    false
                }
            }
        private val binding = ListItemEntryBinding.bind(itemView)
        private lateinit var currentEntry: Entry

        fun bind(entry: Entry) {
            currentEntry = entry
            binding.listItemEntryTVTitle.text = currentEntry.title
            binding.listItemEntryTVContent.text = currentEntry.content
        }

        companion object {
            fun create(parent: ViewGroup): EntryViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_entry, parent, false)
                return EntryViewHolder(itemView)
            }

        }
    }
}