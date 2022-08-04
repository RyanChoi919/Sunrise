package com.nodes.sunrise.ui.entry.read

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.databinding.FragmentEntryReadBinding
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.ViewModelFactory

class EntryReadFragment : Fragment() {

    companion object {
        val KEY_ENTRY = this::class.java.simpleName + ".ENTRY"
    }

    private var _binding: FragmentEntryReadBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntryReadViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry_read, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        /* bundle 에서 Entry 받아와서 viewModel 내 Entry 초기화 */
        viewModel.currentEntry =
            requireArguments().getSerializable(KEY_ENTRY) as Entry

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMenu()
    }

    private fun createMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.frag_entry_read_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.frag_entry_write_menu_edit -> {
                        NavigationHelper(parentFragment!!).navigateToEntryWriteFragmentToModify(
                            viewModel.currentEntry
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}