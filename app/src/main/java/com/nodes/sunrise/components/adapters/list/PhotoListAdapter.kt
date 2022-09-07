package com.nodes.sunrise.components.adapters.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nodes.sunrise.components.comparators.UriComparator
import com.nodes.sunrise.databinding.ListItemPhotoBinding

class PhotoListAdapter : ListAdapter<Uri, PhotoListAdapter.PhotoViewHolder>(UriComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(root).load(currentList[position])
                .into(listItemPhotoPV)
        }
    }

    class PhotoViewHolder(val binding: ListItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): PhotoViewHolder {
                val binding = ListItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PhotoViewHolder(binding)
            }
        }
    }
}