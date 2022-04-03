package com.drax.sendit.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.drax.sendit.R
import com.drax.sendit.domain.network.model.type.UserType.Companion.UserType_NORMAL
import com.drax.sendit.domain.network.model.type.UserType.Companion.UserType_VIP
import com.drax.sendit.view.main.MainVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val mainVM: MainVM by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initActivity()
    }

    private fun initActivity() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        lifecycleScope.launchWhenCreated {
            mainVM.uiState.collect {uiState->
                when(uiState){
                    MainUiState.Neutral -> Unit
                    MainUiState.UserSignedIn -> userSignedIn(bottomNavigationView, navController)
                    MainUiState.UserSignedOut -> userSignedOut(bottomNavigationView, navController)
                }
            }
        }
    }

    private fun userSignedIn(bottomNavigationView: BottomNavigationView, navController: NavController){
        bottomNavigationView.visibility = View.VISIBLE
        navigateToFirstPage(navController)
    }

    private fun userSignedOut(bottomNavigationView: BottomNavigationView, navController: NavController){
        bottomNavigationView.visibility = View.GONE
        navigateToLogin(navController)
    }

    private fun navigateToLogin(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_down)
            .setPopExitAnim(R.anim.slide_up)
            .setPopUpTo(R.id.main_graph, true)
            .build()
        navController.navigate(R.id.loginFragment, null, navOptions)
    }

    private fun navigateToFirstPage(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_down)
            .setPopExitAnim(R.anim.slide_up)
            .setPopUpTo(R.id.main_graph, true)
            .build()
        navController.navigate(navController.graph.startDestination, null, navOptions)
    }
}