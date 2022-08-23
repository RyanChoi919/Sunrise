package com.nodes.sunrise

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.ActivityMainBinding
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
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

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            if (arguments != null) {
                binding.toolbar.title = ""
                binding.fab.isVisible = arguments.getBoolean(getString(R.string.arg_show_fab))
            }
            when(destination.id) {
                R.id.nav_home -> {
                    setTitleLarge("S U N R I S E")
                }
            }
        }

        setOnClickListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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

    fun setTitleLarge(titleString: String?) {
        binding.toolbarTitleLarge.visibility = View.VISIBLE
        binding.toolbarTitleLarge.text = titleString

        binding.toolbarTitleMedium.visibility = View.GONE
        binding.toolbarSubtitle.visibility = View.GONE
    }

    fun setTitleSmallAndSubtitle(titleString: String, subtitleString: String?) {
        binding.toolbarTitleMedium.visibility = View.VISIBLE
        binding.toolbarTitleMedium.text = titleString

        binding.toolbarTitleLarge.visibility = View.GONE

        if (subtitleString == null) {
            binding.toolbarSubtitle.visibility = View.GONE
        } else {
            binding.toolbarSubtitle.visibility = View.VISIBLE
            binding.toolbarSubtitle.text = subtitleString
        }
    }
}