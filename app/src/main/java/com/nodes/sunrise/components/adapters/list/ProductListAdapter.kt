package com.nodes.sunrise.components.adapters.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.nodes.sunrise.components.comparators.ProductWithResultComparator
import com.nodes.sunrise.databinding.ListItemPurchaseItemBinding
import com.nodes.sunrise.model.ProductWithResult

class ProductListAdapter(private val productPurchaseListener: ProductPurchaseListener) :
    ListAdapter<ProductWithResult, ProductListAdapter.PurchaseItemViewHolder>(
        ProductWithResultComparator()
    ) {

    companion object {
        const val TAG = "PurchaseItemListAdapter.TAG"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseItemViewHolder {
        return PurchaseItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PurchaseItemViewHolder, position: Int) {
        val currentProduct = currentList[position]
        Log.d(TAG, "onBindViewHolder: $currentProduct")

        with(holder.binding) {
            listItemPurchaseItemTVName.text = currentProduct.productDetails.name
            listItemPurchaseItemTVDescription.text = currentProduct.productDetails.description
            listItemPurchaseItemBTNPurchase.apply {
                if (currentProduct.isPurchased) {
                    text = "구매 완료"
                    isEnabled = false
                    isClickable = false
                } else {
                    text = currentProduct.productDetails.oneTimePurchaseOfferDetails!!.formattedPrice
                    isEnabled = true
                    isClickable = true
                }
                setOnClickListener { productPurchaseListener.onBillingFlowRequest(currentProduct.productDetails) }
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

    interface ProductPurchaseListener {
        fun onBillingFlowRequest(productDetails: ProductDetails)
    }
}