package com.drax.sendit.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.drax.sendit.R
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_ANDROID
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_CHROME
import com.drax.sendit.view.util.modal
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.drax.modal.Modal
import ir.drax.modal.model.MoButton
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
                    MainUiState.NoConnectionModal -> modal(ModalMessage.Neutral(R.string.no_connected_devices))
                    is MainUiState.ShareModalDisplayed -> displayShareModal(uiState.shareText,uiState.connections)
                    MainUiState.Sharing -> findViewById<View?>(R.id.loading).visibility = View.VISIBLE
                    MainUiState.SharingDone -> {
                        findViewById<View?>(R.id.loading).visibility = View.GONE
                        modal(ModalMessage.Success(R.string.share_success))
                    }
                    is MainUiState.SharingFailed -> {
                        findViewById<View?>(R.id.loading).visibility = View.GONE
                        modal(ModalMessage.FromNetError(uiState.reason.errorCode))
                    }
                }
            }
        }
    }

    override fun onNewIntent(newIntent: Intent?) {
        newIntent?.let {
            onSharedIntent(newIntent)
        }
        super.onNewIntent(newIntent)
    }


    private fun onSharedIntent(newIntent: Intent) {
        val receivedAction = newIntent.action
        val receivedType = newIntent.type
        if (receivedAction == Intent.ACTION_SEND &&
            receivedType != null) {

            // check mime type
            if (receivedType.startsWith("text/")) {
                newIntent.getStringExtra(Intent.EXTRA_TEXT)?.let {receivedText->
                    mainVM.displayShareModal(receivedText)
                }
            }
//            else if (receivedType.startsWith("image/")) {
//                val receiveUri: Uri? = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri?
//                if (receiveUri != null) {
//                    //do your stuff
//                }
//            }
        }
    }

    private fun displayShareModal(receivedText: String, connections: List<Connection>) {
        Modal.builder(this).apply {
            title = getString(R.string.share_modal_title)
            icon = R.drawable.ic_baseline_share_24
            type = Modal.Type.List
            blurEnabled = false

            list = connections.map {connection->
                MoButton(connection.name, platformToIcon(connection.platform)){
                    mainVM.share(receivedText, connection.id)
                    true
                }
            }
        }.build().show()
    }

    private fun platformToIcon(@DevicePlatform platform: Int) = when(platform){
        DevicePlatform_ANDROID -> R.drawable.ic_round_android_24
        DevicePlatform_CHROME -> R.drawable.ic_google_icon
        else -> R.drawable.ic_fragment_devices

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