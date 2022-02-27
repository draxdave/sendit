package com.drax.sendit.data.service

import android.content.*
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.drax.sendit.R
import com.drax.sendit.domain.repo.PushRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject


class SmsReceiver : BroadcastReceiver() {
    private val pushRepository: PushRepository by inject(PushRepository::class.java)

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED" && context != null) {
            val bundle = intent.extras //---get the SMS message passed in---
            var msgs: Array<SmsMessage?>? = null
            var msg_from: String
            if (bundle != null) {
                val key = "رمز"
                val ignoreKey = "اعتبار"

                //---retrieve the SMS message received---
                try {
                    val pdus = bundle["pdus"] as Array<*>?
                    val messages = mutableListOf<String>()
                    var lastSender:String? = null

                    pdus?.toList()?.map { SmsMessage.createFromPdu(it as ByteArray) }
                            ?.forEach {
                                if(messages.isEmpty() || it.originatingAddress!=lastSender)
                                    messages.add(it.messageBody)
                                else
                                    messages[messages.size-1] = messages.last() + it.messageBody

                                lastSender = it.originatingAddress
                            }

                    messages.forEach {line->
                        line
                                .lines()
                                .firstOrNull { (it.contains(ignoreKey).not()) && it.contains(key) }
                                ?.lines()
                                ?.lastOrNull()
                                ?.let{
                                    extractPass(context,it)
                                }
                    }

                } catch (e: Exception) {
                    Log.d("Exception caught", e.message!!)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun extractPass(context: Context,line: String) {
        var passcode = ""
        line.reversed().forEach {
            if(it.toString().matches(Regex("^[0-9|۰-۹]$")))
                passcode = it + passcode
        }
        val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",passcode)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, R.string.code_copied, Toast.LENGTH_LONG).show()

        GlobalScope.launch(Dispatchers.IO) {
            pushRepository.sendContentToAll(passcode)
        }
    }
}