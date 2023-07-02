package com.drax.sendit.view.main

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import app.siamak.sendit.R
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.data.service.NotificationUtil
import com.drax.sendit.data.service.models.NotificationData
import com.drax.sendit.data.service.models.NotificationModel
import com.drax.sendit.view.shareContent.ShareContentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainVM: MainVM by viewModels()
    private lateinit var navController: NavController
    private val nav_host_fragment_container = Random().nextInt()

    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                MainActivityScreen()
            }
        })
    }

    @Composable
    fun MainActivityScreen() {
        val uiState by mainVM.uiState.collectAsState()
        Log.e("MainActivity", "MainActivityScreen: $uiState")

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {

            ConstraintLayout {
                val (bottomNavRef, navHostRef) = createRefs()

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(navHostRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(bottomNavRef.top)
                        },
                    factory = { context ->
                        FragmentContainerView(context).apply {
                            id = nav_host_fragment_container
                        }
                    }) {
                    val navHostFragment = NavHostFragment.create(R.navigation.logged_in_graph)
                    supportFragmentManager
                        .beginTransaction()
                        .replace(nav_host_fragment_container, navHostFragment)
                        .setPrimaryNavigationFragment(navHostFragment)
                        .commit()

                    supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
                        if (uiState is MainUiState.UserSignedIn) {
                            navController = fragment.findNavController()
                            navigateToHomePage(navController)
                        }
                    }
                }

                if (uiState is MainUiState.UserSignedIn)
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .constrainAs(bottomNavRef) {
                                bottom.linkTo(parent.bottom)
                            },
                        factory = {
                            BottomNavigationView(it).apply {
                                inflateMenu(R.menu.bottom_nav)
                                itemTextColor =
                                    ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
                                itemIconTintList =
                                    ColorStateList.valueOf(R.drawable.navigation_view_colored)
                            }
                        }) { bottomNav ->
                        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->

                            NavigationUI.setupWithNavController(
                                bottomNav,
                                fragment.findNavController()
                            )
                        }
                    }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewMainActivityScreen() {
        MainActivityScreen()
    }

    override fun onNewIntent(newIntent: Intent?) {
        newIntent?.let {
            if (mainVM.uiState.value is MainUiState.UserSignedIn) {
                handleNewIntent(newIntent)
            }
        }
        super.onNewIntent(newIntent)
    }

    private fun handleNewIntent(newIntent: Intent) {
        if (newIntent.action == Intent.ACTION_SEND) {
            analytics.set(Event.Share.Requested)
            onSharedIntent(newIntent)

        } else {
            newIntent.extras?.let { extra ->
                (extra.getSerializable(NotificationUtil.NOTIFICATION_DATA) as? NotificationModel)
                    ?.data?.let { notificationData ->
                        analytics.set(Event.Notification.Clicked(newIntent.extras.toString()))

                        when (notificationData) {
                            is NotificationData.Transaction -> navigateTransactions(
                                navController,
                                extra
                            )
                        }
                    }
            }
        }
    }

    private fun onSharedIntent(newIntent: Intent) {
        val receivedType = newIntent.type
        if (receivedType != null && receivedType.startsWith("text/"))
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

    private fun navigateToHomePage(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_down)
            .setPopExitAnim(R.anim.slide_up)
            .setPopUpTo(R.id.main_graph, true)
            .build()
        navController.navigate(R.id.devicesFragment, null, navOptions)
    }

    private fun navigateTransactions(navController: NavController, args: Bundle) {
        navController.navigate(R.id.transmissionsFragment, args)
    }
}