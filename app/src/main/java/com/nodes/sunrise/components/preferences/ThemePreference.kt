package com.nodes.sunrise.components.preferences

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.preference.ListPreference
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.enums.InAppProduct

class ThemePreference(context: Context, attr: AttributeSet) : ListPreference(context, attr) {

    companion object {
        private const val TAG = "ThemePreference.TAG"
    }

    override fun onClick() {
        Log.d(TAG, "onClick: called")
        if (onPreferenceClickListener != null) {
            val isThemeProductPurchased =
                SharedPreferenceHelper(context).getProductPurchaseResult(InAppProduct.THEMING.productId)
            Log.d(TAG, "onClick: isThemeProductPurchased? = $isThemeProductPurchased")
            if (!isThemeProductPurchased) {
                return
            }
        }

        super.onClick()
    }
}