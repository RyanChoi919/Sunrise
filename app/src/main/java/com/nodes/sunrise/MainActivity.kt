package com.nodes.sunrise

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.nodes.sunrise.components.helpers.BillingHelper
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.components.helpers.NotificationHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.ActivityMainBinding
import com.nodes.sunrise.enums.InAppProduct
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity.TAG"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels {
        val repository = (this.application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    lateinit var billingHelper: BillingHelper

    private val onPrefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { pref, key ->
            Log.d(TAG, "OnSharedPreferenceChangeListener : currentKey = $key ")
            when (key) {
                getString(R.string.pref_theme_key) -> applyCurrentThemeMode()
                getString(R.string.pref_notification_enabled_key) -> {
                    val isNotificationEnabled = pref.getBoolean(key, false)
                    if (isNotificationEnabled) {
                        NotificationHelper(this).setNotificationRepeating()
                        Log.d(TAG, "onPrefChangeListener : Notification Repeating set")
                    } else {
                        NotificationHelper(this).cancelNotificationRepeating()
                        Log.d(TAG, "onPrefChangeListener : Notification Repeating canceled")
                    }
                }
                getString(R.string.pref_notification_dow_key),
                getString(R.string.pref_notification_time_key) -> {
                    NotificationHelper(this).updateNotificationRepeating()
                    Log.d(TAG, "onPrefChangeListener : Notification Repeating updated")
                }
                InAppProduct.REMOVE_AD.productId -> {
                    updateRemoveAdsView(pref.getBoolean(key, false))
                }
                else -> { // do nothing }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        applyCurrentThemeMode()
        applyCurrentFontSetting()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            if (arguments != null) {
                binding.fab.isVisible = arguments.getBoolean(getString(R.string.arg_show_fab))
            }
        }

        setOnClickListeners()

        val notificationHelper = NotificationHelper(this)
        with(notificationHelper) {
            createNotificationChannels()
        }

        // Google 모바일 광고 SDK 초기화
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        val prefHelper = SharedPreferenceHelper(this)

        // billing helper 초기화
        billingHelper = BillingHelper(this, lifecycleScope, object : BillingHelper.BillingCallback {
            override fun onBillingClientIsReady() {
                Log.d(TAG, "onBillingClientIsReady: called")
                billingHelper.assertPurchases(BillingClient.ProductType.INAPP)

                /* check purchased */
                for (product in InAppProduct.values()) {
                    when(product) {
                        InAppProduct.REMOVE_AD -> {
                            billingHelper.checkPurchased(product.productId) {
                                updateRemoveAdsView(it)
                                prefHelper.saveProductPurchaseResult(product.productId, it)
                            }
                        }
                        InAppProduct.THEMING -> {
                            billingHelper.checkPurchased(product.productId) {
                                prefHelper.saveProductPurchaseResult(product.productId, it)
                            }
                        }
                    }
                }
            }

            override fun onSuccess(purchase: Purchase) {
                for (product in purchase.products) {
                    billingHelper.checkPurchased(product) {
                        prefHelper.saveProductPurchaseResult(product, it)
                    }
                    Log.d(TAG, "onSuccess: product = $product")
                }
            }

            override fun onFailure(errorCode: Int) {
                Log.d(TAG, "onFailure: errorCode = $errorCode")
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.fab -> {
                val challengeId = SharedPreferenceHelper(this).getSavedChallengeId()
                if (challengeId != null) {
                    lifecycleScope.launch {
                        val challenge =
                            viewModel.repository.challengeDao.getChallengeById(challengeId).first()
                        NavigationHelper(navController).navigateToChallengeCheckFragment(challenge)
                    }
                } else {
                    NavigationHelper(navController).navigateToEntryWriteFragmentToCreate()
                }

            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            fab.setOnClickListener(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        /* check purchased */
        val prefHelper = SharedPreferenceHelper(this)

        for (product in InAppProduct.values()) {
            when(product) {
                InAppProduct.REMOVE_AD -> {
                    billingHelper.checkPurchased(product.productId) {
                        updateRemoveAdsView(it)
                        prefHelper.saveProductPurchaseResult(product.productId, it)
                    }
                }
                InAppProduct.THEMING -> {
                    billingHelper.checkPurchased(product.productId) {
                        prefHelper.saveProductPurchaseResult(product.productId, it)
                    }
                }
            }
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(onPrefChangeListener)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(onPrefChangeListener)
    }

    private fun applyCurrentThemeMode() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val currentThemeMode = pref.getString(getString(R.string.pref_theme_key), null)
        val themeValueArray = resources.getStringArray(R.array.pref_theme_values)

        when (themeValueArray.indexOf(currentThemeMode)) {
            0 -> {
                setDefaultNightMode(MODE_NIGHT_NO)
            }
            1 -> {
                setDefaultNightMode(MODE_NIGHT_YES)
            }
            2 -> {
                setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun applyCurrentFontSetting() {
        val prefHelper = SharedPreferenceHelper(this)
        val currentFont = prefHelper.getSavedFont()
        val fontStyle = when (currentFont) {
            R.font.nanum_gothic -> R.style.Theme_Sunrise_Font_NanumGothic
            R.font.gamja_flower -> R.style.Theme_Sunrise_Font_GamjaFlower
            R.font.single_day -> R.style.Theme_Sunrise_Font_SingleDay
            else -> R.style.Theme_Sunrise_Font_NanumMyeongjo
        }

        theme.applyStyle(fontStyle, true)
    }

    private fun updateRemoveAdsView(isPurchased : Boolean) {
        Log.d(TAG, "updateRemoveAdsView: called, isRemoveAdsPurchased = $isPurchased")
        binding.adView.visibility = if (isPurchased) View.GONE else View.VISIBLE
    }
}