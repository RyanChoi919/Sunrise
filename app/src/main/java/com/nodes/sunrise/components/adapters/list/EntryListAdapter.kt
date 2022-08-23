package com.nodes.sunrise.components.adapters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.comparators.EntriesWithYearMonthComparator
import com.nodes.sunrise.components.listeners.OnEntityClickListener
import com.nodes.sunrise.components.listeners.OnEntityLongClickListener
import com.nodes.sunrise.components.utils.DateUtil
import com.nodes.sunrise.databinding.ListItemEntryBinding
import com.nodes.sunrise.databinding.ListItemEntryGroupBinding
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.EntryViewType
import com.nodes.sunrise.model.EntryAndYearMonth

class EntryListAdapter :
    ListAdapter<EntryAndYearMonth, RecyclerView.ViewHolder>(
        EntriesWithYearMonthComparator()
    ) {

    lateinit var onClickListener: OnEntityClickListener<Entry>
    lateinit var onLongClickListener: OnEntityLongClickListener<Entry>

    init {
        setHasStableIds(true) // notifyDatasetChanged 가 호출될 때 RV가 깜빡이는 것을 방지
    }

    override fun getItemId(position: Int): Long {
        // notifyDatasetChanged 가 호출될 때 RV가 깜빡이는 것을 방지하기 위해 override 된 메소드
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EntryViewType.DAY.ordinal -> {
                EntryDayViewHolder.create(parent).apply {
                    this.onClickListener = this@EntryListAdapter.onClickListener
                    this.onLongClickListener = this@EntryListAdapter.onLongClickListener
                }
            }
            else -> {
                EntryYearMonthViewHolder.create(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EntryViewType.DAY.ordinal -> {
                with((holder as EntryDayViewHolder).binding) {
                    val currentEntry = getItem(position).entry
                    if (currentEntry != null) {
                        holder.currentEntry = currentEntry

                        listItemEntryTVDate.text = currentEntry.dateTime.dayOfMonth.toString()
                        listItemEntryTVDayOfWeek.text = DateUtil.getLocalizedDayOfWeekString(
                            root.context, currentEntry.dateTime.dayOfWeek
                        )
                        listItemEntryTVTitle.visibility =
                            if (currentEntry.isTitleEnabled) View.VISIBLE else View.GONE
                        listItemEntryTVTitle.text = currentEntry.title
                        listItemEntryTVContent.text = currentEntry.content
                    }
                }
            }
            else -> {
                with((holder as EntryYearMonthViewHolder).binding) {
                    val currentYearMonth = getItem(position).yearMonth
                    if (currentYearMonth != null) {
                        listItemEntryGroupTVMonth.text = DateUtil.getLocalizedMonthString(
                            root.context, currentYearMonth)
                        listItemEntryGroupTVYear.text = currentYearMonth.year.toString()
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val current = getItem(position)
        return current.viewType.ordinal
    }

    class EntryDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var _onClickListener: OnEntityClickListener<Entry>? = null
        var onClickListener: OnEntityClickListener<Entry>
            get() = _onClickListener!!
            set(value) {
                _onClickListener = value
                binding.root.setOnClickListener {
                    if (::currentEntry.isInitialized) {
                        value.onClick(it, absoluteAdapterPosition, currentEntry)
                    }
                }
            }
        private var _onLongClickListener: OnEntityLongClickListener<Entry>? = null
        var onLongClickListener: OnEntityLongClickListener<Entry>
            get() = _onLongClickListener!!
            set(value) {
                _onLongClickListener = value
                binding.root.setOnLongClickListener {
                    if (::currentEntry.isInitialized) {
                        value.onItemLongClick(it, absoluteAdapterPosition, currentEntry)
                    }
                    false
                }
            }
        val binding = ListItemEntryBinding.bind(itemView)
        lateinit var currentEntry: Entry

        companion object {
            fun create(parent: ViewGroup): EntryDayViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_entry, parent, false)
                return EntryDayViewHolder(itemView)
            }
        }
    }

    class EntryYearMonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ListItemEntryGroupBinding.bind(itemView)

        companion object {
            fun create(parent: ViewGroup): EntryYearMonthViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_entry_group, parent, false)
                return EntryYearMonthViewHolder(itemView)
            }
        }
    }
}