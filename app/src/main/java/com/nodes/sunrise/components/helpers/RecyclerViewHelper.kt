package com.nodes.sunrise.components.helpers


import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.EntryListAdapter
import com.nodes.sunrise.components.decorators.ListMarginDecorator
import com.nodes.sunrise.components.listeners.OnEntryClickListener
import com.nodes.sunrise.components.listeners.OnEntryLongClickListener
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch

class RecyclerViewHelper<T : BaseViewModel>(val fragment: Fragment, val viewModel: T) {

    val navigationHelper = NavigationHelper(fragment)

    fun setRecyclerViewWithLiveData(
        rv: RecyclerView, adapter: EntryListAdapter, data: LiveData<List<Entry>>
    ) {
        setRecyclerView(rv, adapter)
        data.observe(fragment.viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    fun setRecyclerView(rv: RecyclerView, adapter: EntryListAdapter) {
        with(rv) {
            setRecyclerViewAttributes(rv)
            adapter.onEntryClickListener = object : OnEntryClickListener {
                override fun onClick(view: View, pos: Int, entry: Entry) {
                    navigationHelper.navigateToEntryReadFragment(entry)
                }
            }
            adapter.onEntryLongClickListener = object : OnEntryLongClickListener {
                override fun onItemLongClick(view: View, pos: Int, entry: Entry) {
                    showLongClickListDialog(entry)
                }
            }
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