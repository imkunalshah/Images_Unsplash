package com.kunal.sunbase_task.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.kunal.sunbase_task.R
import com.kunal.sunbase_task.databinding.ActivityMainBinding
import com.kunal.sunbase_task.ui.base.BaseActivity
import com.kunal.sunbase_task.ui.fragments.HomeFragment
import com.kunal.sunbase_task.ui.fragments.QuitAppDialogFragment
import com.kunal.sunbase_task.ui.fragments.SearchFragment
import com.kunal.sunbase_task.ui.viewmodels.ImageViewModel
import com.kunal.sunbase_task.data.network.ConnectionLiveData
import com.kunal.sunbase_task.utils.showNetworkUnavailableSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    NavigationBarView.OnItemSelectedListener {

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData

    private val viewModel: ImageViewModel by viewModels()

    private val fragments: Array<Fragment> get() = arrayOf(homeFragment, searchFragment)
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private var selectedFragmentIndex = 0

    companion object {
        const val SELECTED_FRAGMENT = "selectedFragmentIndex"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFragments(savedInstanceState)
    }

    private fun initializeFragments(savedInstanceState: Bundle?) {
        when (savedInstanceState) {
            null -> {
                val homeFragment = HomeFragment.newInstance().also { this.homeFragment = it }
                val searchFragment = SearchFragment.newInstance().also { this.searchFragment = it }
                supportFragmentManager.beginTransaction()
                    .add(binding.fragmentContainer.id, homeFragment, HomeFragment.TAG)
                    .add(binding.fragmentContainer.id, searchFragment, SearchFragment.TAG)
                    .selectFragment(selectedFragmentIndex)
                    .commit()
            }
            else -> {
                selectedFragmentIndex = savedInstanceState.getInt(SELECTED_FRAGMENT, 0)
                homeFragment =
                    supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment
                searchFragment =
                    supportFragmentManager.findFragmentByTag(SearchFragment.TAG) as SearchFragment
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                selectFragment(0)
                return true
            }
            R.id.search -> {
                selectFragment(1)
                return true
            }
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_FRAGMENT, selectedFragmentIndex)
    }


    override fun initializeViews() {
        binding.bottomNavBar.setOnItemSelectedListener(this)
    }

    override fun initializeObservers() {
        initializeNetworkConnectionObserver()
    }

    private fun FragmentTransaction.selectFragment(selectedIndex: Int): FragmentTransaction {
        fragments.forEachIndexed { index, fragment ->
            if (index == selectedIndex) {
                attach(fragment)
            } else {
                detach(fragment)
            }
        }
        return this
    }

    private fun selectFragment(indexToSelect: Int) {
        this.selectedFragmentIndex = indexToSelect

        supportFragmentManager.beginTransaction()
            .selectFragment(indexToSelect)
            .commit()
    }

    override fun onBackPressed() {
        val quitAppDialogFragment = QuitAppDialogFragment.newInstance().apply {
            isCancelable = false
        }.also {
            it.onQuitClicked = {
                super.onBackPressed()
            }
        }
        quitAppDialogFragment.show(supportFragmentManager, QuitAppDialogFragment.TAG)
    }

    private fun initializeNetworkConnectionObserver() {
        connectionLiveData.observeForever { isConnected ->
            when (isConnected) {
                true -> {
                    viewModel.isConnected = true
                }
                false -> {
                    viewModel.isConnected = false
                    binding.root.showNetworkUnavailableSnackBar {}
                }
            }
        }
    }

}