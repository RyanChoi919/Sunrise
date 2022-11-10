package com.nodes.sunrise.components.helpers

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.android.billingclient.api.*
import com.nodes.sunrise.enums.InAppProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingHelper(
    private val activity: Activity,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val callback: BillingCallback
) {

    companion object {
        private const val TAG = "BillingHelper.TAG"
    }

    /* 구매 관련 업데이트를 수신하는 리스너 */
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when {
            billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null -> {
                /* 상품 구매가 완료된 경우, 구매 확인 처리 (구매 후 3일 이내 구매 확인하지 않으면 자동 환불) */
                for (purchase in purchases) {
                    confirmPurchase(purchase)
                }
            }
            else -> {
                /* 구매 실패 */
                callback.onFailure(billingResult.responseCode)
            }
        }
    }

    private var billingClient: BillingClient = BillingClient.newBuilder(activity)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "onBillingSetupFinished: OK")
                    callback.onBillingClientIsReady()
                } else {
                    Log.d(TAG, "onBillingSetupFinished: failed")
                    callback.onFailure(billingResult.responseCode)
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.e(TAG, "onBillingServiceDisconnected: called")
            }
        })
    }

    /**
     * 구매된 항목에 대해 승인을 진행하는 메소드
     *
     * @param purchase : 구매 내역
     * */
    private fun confirmPurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            // 구매는 완료되었으나 확인(acknowledge)이 되어있지 않다면 구매 확인 처리
            val ackPurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)

            lifecycleScope.launch(Dispatchers.IO) {
                val result = billingClient.acknowledgePurchase(ackPurchaseParams.build())
                withContext(Dispatchers.Main) {
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        callback.onSuccess(purchase)
                    } else {
                        callback.onFailure(result.responseCode)
                    }
                }
            }
        }
    }

    /**
     * Google Play Console에 입력된 Product 정보를 Query하는 메소드
     * @param result : Query 결과(List<ProductDetails>)를 처리하며 return 값은 없는 람다
     * */
    fun queryProductDetailsResult(result: (List<ProductDetails>) -> Unit = {}) {
        Log.d(TAG, "queryProductDetail: called")
        /* product List 생성 */
        val productList = ArrayList<QueryProductDetailsParams.Product>().apply {
            for (p in InAppProduct.values()) {
                add(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(p.productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            }
        }

        /* QueryProductDetailParams 생성 */
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)

        /* 결과값 전송 */
        lifecycleScope.launch(Dispatchers.IO) {
            val productDetailsResult = billingClient.queryProductDetails(params.build())
            Log.d(TAG, "queryProductDetail: ${productDetailsResult.productDetailsList}")

            withContext(Dispatchers.Main) {
                result(productDetailsResult.productDetailsList ?: emptyList())
            }
        }
    }

    /**
     * 구매를 처리하는 메소드
     * @param productDetails : 구매를 진행하려는 productDetails
     * */
    fun purchase(productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        /* 구매 flow 진행 (billingClient의 launchBillingFlow를 호출하여 시작) */
        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

        /* 구매 flow가 처리된 경우, PurchasesUpdatedListener를 통해 이후 과정 진행*/

        /* 구매 flow가 처리되지 않은 경우, callback을 통해 responseCode 전달*/
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            callback.onFailure(billingResult.responseCode)
        }
    }

    /**
     * 비정상적인 원인으로 purchase가 confirm되지 않는 경우에 대비하기 위한 메소드.
     *
     * @param type : ProductType (BillingClient.ProductType.INAPP, BillingClient.ProductType.SUBS)
     * */
    fun assertPurchases(type: String) {
        if (billingClient.isReady) {
            val params = QueryPurchasesParams.newBuilder().setProductType(type)
            lifecycleScope.launch {
                billingClient.queryPurchasesAsync(params.build()).purchasesList.let {
                    for (purchase in it) {
                        Log.d(TAG, "assertPurchases: purchase = $purchase")
                        if (!purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            confirmPurchase(purchase)
                        }
                    }
                }
            }
        }
    }

    /**
     * 구매 여부 체크, 소비성 구매가 아닌 항목에 한정.
     * @param productId : 구매 여부를 확인할 Product의 Id
     */
    fun checkPurchased(
        productId: String,
        result: (purchased: Boolean) -> Unit
    ) {
        lifecycleScope.launch {
            val params =
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)

            billingClient.queryPurchasesAsync(params.build()).purchasesList.let { purchaseList ->
                Log.d(TAG, "checkPurchased: purchaseList = $purchaseList")
                var isPurchased = false
                for (purchase in purchaseList) {
                    if (purchase.products.contains(productId) && purchase.isPurchaseConfirmed()) {
                        isPurchased = true
                    }
                }
                Log.d(TAG, "checkPurchased: productId = $productId, isPurchased = $isPurchased")
                result(isPurchased)
            }
        }
    }

    // 구매 확인 검사 Extension
    private fun Purchase.isPurchaseConfirmed(): Boolean {
        return this.isAcknowledged && this.purchaseState == Purchase.PurchaseState.PURCHASED
    }

    /* 구매 관련 처리 결과를 callback하기 위한 인터페이스*/
    interface BillingCallback {
        fun onBillingClientIsReady()
        fun onSuccess(purchase: Purchase)
        fun onFailure(errorCode: Int)
    }
}