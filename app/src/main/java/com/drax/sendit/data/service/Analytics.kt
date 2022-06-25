package com.drax.sendit.data.service

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

class Analytics(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun set(event: Event) =
        firebaseAnalytics.logEvent(event.category, event.params)
}

sealed class Event {
    abstract val category: String
    abstract val params: Bundle

    sealed class App(override val category: String = "APP") : Event() {
        object Open : App() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to FirebaseAnalytics.Event.APP_OPEN
            )
        }

    }





    sealed class Fragment(override val category: String = "FRAGMENT") : Event() {
        data class Viewed(val fragment: androidx.fragment.app.Fragment): Fragment() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "EVENT_FRAGMENT_VIEWED",
                "FRAGMENT_NAME" to fragment::class.java.name
            )
        }
    }

    sealed class View(override val category: String = "VIEW") : Event() {
        sealed class Clicked(viewEventName: String): View() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "EVENT_VIEW_CLICKED",
                "VIEW_EVENT_NAME" to viewEventName
            )

            object SigninWithGoogle : Clicked("SigninWithGoogle")
        }
    }

    sealed class Other(override val category: String = "Other") : Event() {

    }

    sealed class SignIn(override val category: String = "SignIn"): Event() {

        object LeftSignIn: SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Exit"
            )
        }

        object Succeed: SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Succeed"
            )
        }

        object SsoDone: SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "SSO Succeed"
            )
        }

        object SignInFlowFailed: SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "SignInFlowFailed"
            )
        }
        object SignUpFlowStarted: SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "SignUpFlowFailed"
            )
        }

        object GoToHome: SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "GoToHome"
            )
        }

        data class Failed(val reason: String): SignIn() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Failed",
                "DETAIL" to reason
            )
        }
    }

    sealed class Notification(override val category: String = "Notification"): Event() {
        data class Any(val data: Map<String, String>): Notification() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "All",
                "DATA" to data
            )
        }
        object ConnectionRequest: Notification() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ConnectionRequest"
            )
        }

        object Content: Notification() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Content"
            )
        }

        data class Clicked(val notificationData: String?): Notification() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Clicked",
                "DATA" to notificationData
            )
        }
    }

    sealed class Share(override val category: String = "Share"): Event() {
        object Requested: Share(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Requested"
            )
        }

        object Sent: Share(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Sent"
            )
        }
        data class Failed(val reason: String): Share(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Failed",
                "REASON" to reason
            )
        }
    }

    sealed class QR(override val category: String = "QR"): Event() {
        object Scanned: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Scanned"
            )
        }

        object ScannerRequested: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ScannerRequested"
            )
        }

        object LoadQRFailed: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "LoadQRFailed"
            )
        }
        object LoadQRFailedFromNet: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "LoadQRFailedFromNet"
            )
        }

        object InvitationSent: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "InvitationSent"
            )
        }

        object ReceivedInvitationDialogShown: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ReceivedInvitationDialogShown"
            )
        }
        object ReceivedInvitationRejected: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "InvitationRejected"
            )
        }

        object ReceivedInvitationFailedToSend: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ReceivedInvitationFailedToSend"
            )
        }
        object ReceivedInvitationAccepted: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ReceivedInvitationAccepted"
            )
        }

        object CameraPermissionRejected: QR(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "CameraPermissionRejected"
            )
        }
    }

    sealed class Messages(override val category: String = "Messages"): Event() {
        object Copy: Messages(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Copy"
            )
        }

        object Remove: Messages(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Remove"
            )
        }
        object Share: Messages(){
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Share"
            )
        }
    }

    sealed class Connections(override val category: String = "Connections"): Event() {
        object UnpairRequested: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "UnpairRequested"
            )
        }
        object Unpaired: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Unpaired"
            )
        }
        object PopupShown: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "PopupShown"
            )
        }
        object Accepted: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Accepted"
            )
        }

        object Rejected: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Rejected"
            )
        }

        object RefreshedList: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "RefreshedList"
            )
        }

        object RefreshFailed: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "RefreshFailed"
            )
        }

        object Logout: Connections() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "Logout"
            )
        }
    }

    sealed class Network(override val category: String = "Network"): Event() {
        data class ApiRequest(val apiName: String?): Network() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ApiCalled",
                "NAME" to apiName
            )
        }

        data class ApiError(val errorCode: String?, val request: String?): Network() {
            override val params: Bundle = bundleOf(
                EVENT_KEY to "ApiError",
                "CODE" to errorCode,
                "REQUEST" to request
            )
        }
    }

    companion object {
        const val EVENT_KEY = "EVENT"
    }
}