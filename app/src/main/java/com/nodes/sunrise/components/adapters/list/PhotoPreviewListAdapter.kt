package com.nodes.sunrise.components.adapters.list

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.components.comparators.UriComparator
import com.nodes.sunrise.databinding.ListItemPhotoPreviewBinding

class PhotoPreviewListAdapter(private val onPhotoPreviewListEmptyListener: OnPhotoPreviewListEmptyListener) :
    ListAdapter<Uri, PhotoPreviewListAdapter.PhotoPreviewViewHolder>(UriComparator()) {

    companion object {
        private const val TAG : String = "PhotoPreviewListAdapter.TAG"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPreviewViewHolder {
        return PhotoPreviewViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PhotoPreviewViewHolder, position: Int) {
        val currentUri = currentList[position]
        with(holder.binding) {
            listItemPhotoPreviewIVPhoto.setImageURI(currentUri)
            listItemPhotoPreviewIVClear.setOnClickListener {
                val currentList = currentList.toMutableList()
                currentList.removeAt(holder.absoluteAdapterPosition)
                Log.d(TAG, "onBindViewHolder: isCurrentListEmpty? = ${currentList.isEmpty()}")
                if (currentList.isEmpty()) onPhotoPreviewListEmptyListener.onListEmpty()
                submitList(currentList)
            }
        }
        holder.binding.listItemPhotoPreviewIVPhoto.setImageURI(currentUri)
    }

    class PhotoPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ListItemPhotoPreviewBinding.bind(itemView)

        companion object {
            fun create(parent: ViewGroup): PhotoPreviewViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_photo_preview, parent, false)
                return PhotoPreviewViewHolder(itemView)
            }
        }
    }

    interface OnPhotoPreviewListEmptyListener {
        fun onListEmpty()
    }
}