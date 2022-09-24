package com.drax.sendit.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import app.siamak.sendit.R
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.data.service.NotificationUtil
import com.drax.sendit.data.service.models.NotificationData
import com.drax.sendit.data.service.models.NotificationModel
import com.drax.sendit.view.shareContent.ShareContentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val mainVM: MainVM by viewModel()
    private lateinit var navController: NavController
    private val analytics: Analytics by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initActivity()
    }

    private fun initActivity() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

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
        intent?.let { handleNewIntent(it) }
    }

    override fun onNewIntent(newIntent: Intent?) {
        newIntent?.let { handleNewIntent(newIntent) }
        super.onNewIntent(newIntent)
    }


    private fun handleNewIntent(newIntent: Intent) {
        if (newIntent.action == Intent.ACTION_SEND) {
            analytics.set(Event.Share.Requested)
            onSharedIntent(newIntent)

        } else {
            analytics.set(Event.Notification.Clicked(newIntent.extras.toString()))
            newIntent.extras?.let { extra ->
                (extra.getSerializable(NotificationUtil.NOTIFICATION_DATA) as? NotificationModel)
                    ?.data?.let { notificationData ->
                    when(notificationData){
                        is NotificationData.Transaction -> navigateTransactions(navController, extra)
                    }
                }
            }
        }
    }

    private fun onSharedIntent(newIntent: Intent) {
        val receivedType = newIntent.type
        if(receivedType != null && receivedType.startsWith("text/"))
            newIntent.getStringExtra(Intent.EXTRA_TEXT)?.let { receivedText ->
                launchShareFragment(receivedText)
            }
    }

    private fun launchShareFragment(receivedText: String) {
        supportFragmentManager.setFragmentResultListener(ShareContentFragment.TAG, this) { _, _ ->
            finishAndRemoveTask()
        }

        ShareContentFragment().apply {
            arguments = bundleOf(ShareContentFragment.SHARE_CONTENT_KEY to receivedText)
            show(supportFragmentManager, ShareContentFragment.TAG)
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

    private fun navigateTransactions(navController: NavController, args: Bundle) {
        navController.navigate(R.id.transmissionsFragment, args)
    }
}