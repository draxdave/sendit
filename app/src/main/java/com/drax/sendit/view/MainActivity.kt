package com.drax.sendit.view

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
            mainVM.user.collect {user->
                when{
                    user == null -> {
                        navigateToLogin(navController)
                        bottomNavigationView.visibility = View.GONE
                    }
                    user.type == UserType_NORMAL ||
                            user.type == UserType_VIP->{
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        }
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
}