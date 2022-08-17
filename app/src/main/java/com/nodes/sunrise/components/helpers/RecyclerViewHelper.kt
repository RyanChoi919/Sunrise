package com.nodes.sunrise.components.helpers


import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.ChallengeListAdapter
import com.nodes.sunrise.components.adapters.list.EntryListAdapter
import com.nodes.sunrise.components.decorators.ListMarginDecorator
import com.nodes.sunrise.components.listeners.OnEntityClickListener
import com.nodes.sunrise.components.listeners.OnEntityLongClickListener
import com.nodes.sunrise.db.entity.ChallengeGroupWithChallenges
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.EntryViewType
import com.nodes.sunrise.model.EntryAndYearMonth
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth

class RecyclerViewHelper<T : BaseViewModel>(val fragment: Fragment, val viewModel: T) {

    val navigationHelper = NavigationHelper(fragment.findNavController())

    fun setRecyclerViewWithLiveData(
        rv: RecyclerView, adapter: EntryListAdapter, data: LiveData<List<Entry>>
    ) {
        setRecyclerView(rv, adapter)
        data.observe(fragment.viewLifecycleOwner) {
            val map = HashMap<YearMonth, ArrayList<Entry>>()

            for (entry in it) {
                val currentYearMonth = YearMonth.from(entry.dateTime)
                if (!map.contains(currentYearMonth)) {
                    map[currentYearMonth] = ArrayList()
                }
                map[currentYearMonth]!!.add(entry)
            }

            val list = ArrayList<EntryAndYearMonth>()

            for (item in map) {
                list.add(EntryAndYearMonth(EntryViewType.YEAR_MONTH, yearMonth = item.key))
                for (entry in item.value) {
                    list.add(EntryAndYearMonth(EntryViewType.DAY, entry = entry))
                }
            }

            adapter.submitList(list)
        }
    }

    fun setRecyclerViewWithLiveData(
        rv: RecyclerView,
        adapter: ChallengeListAdapter,
        data: LiveData<List<ChallengeGroupWithChallenges>>,
    ) {
        setRecyclerView(rv, adapter)
        data.observe(fragment.viewLifecycleOwner) {
//            val list = ArrayList<ChallengeAndGroup>()
//
//            for (challengeGroup in it) {
//                list.add(
//                    ChallengeAndGroup(
//                        ChallengeViewType.GROUP,
//                        challengeGroup = challengeGroup.group
//                    )
//                )
//                for (challengeItem in challengeGroup.challenges) {
//                    list.add(
//                        ChallengeAndGroup(
//                            ChallengeViewType.ITEM,
//                            challenge = challengeItem
//                        )
//                    )
//                }
//            }
//
//            adapter.submitList(list)
//            rv.post {
//                val smoothScroller = CenterSmoothScroller(rv.context)
//                smoothScroller.targetPosition = adapter.selectedPosition
//                if (smoothScroller.targetPosition > 0) {
//                    rv.layoutManager!!.startSmoothScroll(smoothScroller)
//                }
//            }
        }
    }

    private fun setRecyclerView(rv: RecyclerView, adapter: EntryListAdapter) {
        with(rv) {
            setRecyclerViewAttributes(rv)
            adapter.onClickListener = object : OnEntityClickListener<Entry> {
                override fun onClick(view: View, pos: Int, entity: Entry) {
                    navigationHelper.navigateToEntryReadFragment(entity)
                }
            }
            adapter.onLongClickListener = object : OnEntityLongClickListener<Entry> {
                override fun onItemLongClick(view: View, pos: Int, entity: Entry) {
                    showLongClickListDialog(entity)
                }
            }
            this.adapter = adapter
        }
    }

    private fun setRecyclerView(rv: RecyclerView, adapter: ChallengeListAdapter) {
        with(rv) {
            setRecyclerViewAttributes(rv)
            this.adapter = adapter
        }
    }

    private fun setRecyclerViewAttributes(rv: RecyclerView) {
        with(rv) {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            addItemDecoration(ListMarginDecorator(resources))
        }
    }

    private fun showLongClickListDialog(entry: Entry) {
        val itemList =
            fragment.requireContext().resources.getStringArray(R.array.entry_long_click_menu_items)

        return AlertDialog.Builder(fragment.requireContext())
            .setItems(itemList) { _, i ->
                when (i) {
                    0 -> navigationHelper.navigateToEntryWriteFragmentToModify(entry)
                    1 -> AlertDialogHelper().showEntryDeleteConfirmDialog(
                        fragment,
                        viewModel,
                        entry
                    )
                }
            }.create().show()
    }


    private fun showEntryDeleteAlertDialog(entry: Entry) {
        val resources = fragment.requireContext().resources

        val title = resources.getString(R.string.entry_delete_dialog_title)
        val message = resources.getString(R.string.entry_delete_dialog_message)
        val confirmToastMessage = resources.getString(R.string.entry_delete_dialog_confirm_toast)
        val deleteText = resources.getString(R.string.entry_delete_dialog_button_delete)
        val cancelText = resources.getString(R.string.entry_delete_dialog_button_cancel)

        AlertDialog.Builder(fragment.requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(deleteText) { _, _ ->
                fragment.requireActivity().lifecycle.coroutineScope.launch {
                    viewModel.delete(entry)
                    Toast.makeText(fragment.context, confirmToastMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(cancelText) { _, _ ->
                // do nothing
            }
            .create().show()
    }
}