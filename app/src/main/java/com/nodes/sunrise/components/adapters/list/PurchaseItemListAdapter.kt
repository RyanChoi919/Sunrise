package com.nodes.sunrise.components.adapters.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.components.comparators.PurchaseItemComparator
import com.nodes.sunrise.databinding.ListItemPurchaseItemBinding
import com.nodes.sunrise.model.PurchaseItem

class PurchaseItemListAdapter :
    ListAdapter<PurchaseItem, PurchaseItemListAdapter.PurchaseItemViewHolder>(PurchaseItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseItemViewHolder {
        return PurchaseItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PurchaseItemViewHolder, position: Int) {
        val currentItem = currentList[position]

        with(holder.binding) {
            listItemPurchaseItemTVName.text = currentItem.name
            listItemPurchaseItemTVDescription.text = currentItem.description
            listItemPurchaseItemBTNPurchase.text = currentItem.price
            if (currentItem.priceOriginal != null) {
                listItemPurchaseItemTVOriginalPrice.visibility = View.VISIBLE
                listItemPurchaseItemTVOriginalPrice.text = currentItem.priceOriginal
                listItemPurchaseItemTVOriginalPrice.paintFlags =
                    listItemPurchaseItemTVOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                listItemPurchaseItemTVOriginalPrice.visibility = View.GONE
            }

        }
    }

    class PurchaseItemViewHolder(val binding: ListItemPurchaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun create(parent: ViewGroup): PurchaseItemViewHolder {
                val binding = ListItemPurchaseItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PurchaseItemViewHolder(binding)
            }
        }
    }
}