package com.nodes.sunrise.ui.entry.write

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class EntryWriteFragment : Fragment(), View.OnClickListener {

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

        setOnClickListeners()

        /* arguments 확인 및 viewModel 데이터 초기화 */
        if (savedInstanceState == null && arguments != null) {
            val entry = requireArguments().getSerializable(KEY_ENTRY) as Entry?

            if (entry != null) {
                isToCreateMode = false
                viewModel.currentEntry.set(requireArguments().getSerializable(KEY_ENTRY) as Entry)
            } else {
                isToCreateMode = true
                val newEntry =
                    Entry(0, LocalDateTime.now(), title = "", content = "", isTitleEnabled = true)
                viewModel.currentEntry.set(newEntry)

            }
        }

        /* 콘텐츠 EditText의 글자 수를 카운트&표시하기 위한 TextWatcher 설정 */
        binding.fragEntryWriteETContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                val textCountString = if (p0 != null && p0.toString() != "") {
                    p0.toString().replace(" ", "").length.toString()
                } else {
                    "0"
                }
                viewModel.textCount.set(textCountString)
            }
        })

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    private fun setOnClickListeners() {
        val views = ArrayList<View>().apply {
            with(binding) {
                add(fragEntryWriteMCBEntryTime)
            }
        }

        for (v in views) {
            v.setOnClickListener(this@EntryWriteFragment)
        }
    }

    override fun onClick(v: View?) {
        // add codes
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
                            parentFragment!!, viewModel, viewModel.currentEntry.get()!!
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}