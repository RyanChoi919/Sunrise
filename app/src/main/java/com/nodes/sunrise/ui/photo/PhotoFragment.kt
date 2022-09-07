package com.nodes.sunrise.ui.photo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.PhotoListAdapter
import com.nodes.sunrise.databinding.FragmentPhotoBinding
import com.nodes.sunrise.ui.BaseFragment

class PhotoFragment : BaseFragment() {

    companion object {
        val KEY_PHOTO = this::class.java.simpleName + ".PHOTO"
    }

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private val uris by lazy {
        val list = requireArguments().get(KEY_PHOTO) as List<*>
        photoTotalCount = list.size
        list.filterIsInstance<Uri>()
    }
    private val adapter = PhotoListAdapter()

    private var photoTotalCount: Int = 0

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setToolbarTitle(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater)

        setToolbar(binding.fragPhotoTB)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle(0)

        adapter.submitList(uris)
        with(binding.fragPhotoVP2) {
            adapter = this@PhotoFragment.adapter
            registerOnPageChangeCallback(onPageChangeCallback)
        }
    }

    private fun setToolbarTitle(currentPosition: Int) {
        binding.fragPhotoTVToolbarTitle.text = String.format(
            resources.getString(R.string.frag_photo_photo_count),
            currentPosition + 1,
            photoTotalCount
        )
    }

    override fun onDestroy() {
        binding.fragPhotoVP2.unregisterOnPageChangeCallback(onPageChangeCallback)
        super.onDestroy()
    }
}