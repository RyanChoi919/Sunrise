package com.nodes.sunrise.ui.entry.write

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.helpers.AlertDialogHelper
import com.nodes.sunrise.databinding.FragmentEntryWriteBinding
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.ViewModelFactory
import java.time.LocalDateTime

class EntryWriteFragment : Fragment() {

    companion object {
        val KEY_ENTRY = this::class.java.simpleName + ".ENTRY"
    }

    private var _binding: FragmentEntryWriteBinding? = null
    private val binding get() = _binding!!

    private var isToCreateMode = true

    private val viewModel: EntryWriteViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry_write, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        if (arguments != null) {
            viewModel.currentEntry = requireArguments().getSerializable(KEY_ENTRY) as Entry
            isToCreateMode = false
        } else {
            viewModel.currentEntry = Entry(0, LocalDateTime.now(), "", true, "")
            isToCreateMode = true
        }

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()

                val menuRes = if (isToCreateMode) {
                    R.menu.frag_entry_write_menu_create
                } else {
                    R.menu.frag_entry_write_menu_modify
                }

                menuInflater.inflate(menuRes, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.frag_entry_write_menu_save -> {
                        viewModel.saveEntry()
                        findNavController().popBackStack()
                        true
                    }
                    R.id.frag_entry_write_menu_modify -> {
                        viewModel.modifyEntry()
                        findNavController().popBackStack()
                        true
                    }
                    R.id.frag_entry_write_menu_delete -> {
                        AlertDialogHelper().showEntryDeleteConfirmDialog(
                            parentFragment!!, viewModel, viewModel.currentEntry
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}