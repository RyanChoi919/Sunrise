package com.nodes.sunrise.components.helpers

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.nodes.sunrise.R
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseViewModel
import kotlinx.coroutines.launch

class AlertDialogHelper {

    fun showEntryDeleteConfirmDialog(
        fragment: Fragment,
        viewModel: BaseViewModel,
        entry: Entry
    ) {
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
                fragment.findNavController().navigate(R.id.HomeFragment)
            }
            .setNegativeButton(cancelText) { _, _ ->
                // do nothing
            }
            .create().show()
    }
}