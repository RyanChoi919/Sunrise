package com.nodes.sunrise.components.helpers

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.nodes.sunrise.R
import com.nodes.sunrise.components.listeners.OnChallengeResultSetListener
import com.nodes.sunrise.components.listeners.OnPermissionRationaleResultListener
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.ChallengeResult
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
                fragment.findNavController().navigate(R.id.nav_home)
            }
            .setNegativeButton(cancelText) { _, _ ->
                // do nothing
            }
            .create().show()
    }

    fun showChangeChallengeResultDialog(
        fragment: Fragment,
        onChallengeResultSetListener: OnChallengeResultSetListener
    ) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle(R.string.change_challenge_result_dialog_title)
            .setItems(
                R.array.change_challenge_result_dialog_items
            ) { dialog, which ->
                val newResult = when (which) {
                    0 -> ChallengeResult.SUCCESS
                    1 -> ChallengeResult.FAIL
                    else -> ChallengeResult.POSTPONE
                }
                onChallengeResultSetListener.onSet(newResult)
            }
            .create().show()
    }

    fun showLocationPermissionRationaleDialog(
        context: Context,
        listener: OnPermissionRationaleResultListener
    ) {
        AlertDialog.Builder(context)
            .setTitle("권한 승인 필요")
            .setMessage("위치 정보를 기록하려면 위치 권한 승인이 필요합니다.")
            .setPositiveButton("확인") { _, _ ->
                listener.onResultSet(true)
            }
            .setNegativeButton("거절") { _, _ ->
                listener.onResultSet(false)
            }.create().show()
    }
}