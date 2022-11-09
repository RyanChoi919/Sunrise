package com.nodes.sunrise.ui.purchases

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.ProductDetails
import com.nodes.sunrise.MainActivity
import com.nodes.sunrise.components.adapters.list.ProductListAdapter
import com.nodes.sunrise.components.decorators.ListMarginDecorator
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.FragmentPurchaseBinding
import com.nodes.sunrise.enums.InAppProduct
import com.nodes.sunrise.enums.ListOrientation
import com.nodes.sunrise.model.ProductWithResult
import com.nodes.sunrise.ui.BaseFragment
import kotlin.streams.toList

class PurchaseFragment : BaseFragment() {

    companion object {
        const val TAG = "PurchaseFragment.TAG"
    }

    private var _binding: FragmentPurchaseBinding? = null
    val binding: FragmentPurchaseBinding
        get() = _binding!!

    private val billingHelper by lazy {
        (requireActivity() as MainActivity).billingHelper
    }
    private val productListAdapter =
        ProductListAdapter(object : ProductListAdapter.ProductPurchaseListener {

            /* Product List의 금액 버튼을 클릭했을 때, billingHelper를 통해 구매 Flow 를 진행*/
            override fun onBillingFlowRequest(productDetails: ProductDetails) {
                billingHelper.purchase(productDetails)
            }
        })

    private val productWithResultList = MutableLiveData<List<ProductWithResult>>()

    private val preferenceHelper by lazy {
        SharedPreferenceHelper(requireContext())
    }

    private val onPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { pref, key ->
            Log.d(TAG, "onPreferenceChangeListener: currentKey = $key")
            when (key) {
                InAppProduct.REMOVE_AD.productId,
                InAppProduct.THEMING.productId -> {
                    val value = pref.getBoolean(key, false)
                    val list = productWithResultList.value!!
                    list.find { it.productDetails.productId == key }?.isPurchased = value
                    Log.d(TAG, "onPreferenceChangeListener : modifiedList = $list ")
                    productWithResultList.value = list
                }
                else -> {
                    // do nothing
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseBinding.inflate(inflater)

        setToolbarBinding(binding.fragPurchaseTB)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        productWithResultList.observe(viewLifecycleOwner) { newList ->
            productListAdapter.submitList(newList.map { it.copy() })
        }

        billingHelper.queryProductDetailsResult {
            Log.d(TAG, "onViewCreated: queriedProductDetails = $it")
            if (it.isNotEmpty()) {
                productWithResultList.value =
                    it.stream().map { productDetails ->
                        ProductWithResult(
                            productDetails,
                            preferenceHelper.getProductPurchaseResult(productDetails.productId)
                        )
                    }.toList()
            } else {
                Toast.makeText(
                    requireContext(),
                    "상품 정보를 확인할 수 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun setRecyclerView() {
        val rv = binding.fragPurchaseRVPurchaseItems
        with(rv) {
            this.adapter = productListAdapter
            isNestedScrollingEnabled = false
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(ListMarginDecorator(resources, ListOrientation.VERTICAL))
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: called")
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(onPreferenceChangeListener)
    }

    override fun onDestroy() {
        Log.d(TAG, "onResume: called")
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(onPreferenceChangeListener)
    }
}