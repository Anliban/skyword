package anliban.skyword.data.source.remote

import anliban.skyword.data.source.local.PrefModel
import com.google.firebase.messaging.FirebaseMessagingService

class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val pref by lazy { PrefModel(applicationContext) }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        pref.fcmToken = token
    }
}
