package com.nodes.sunrise

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.ActivityMainBinding
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels {
        val repository = (this.application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
}