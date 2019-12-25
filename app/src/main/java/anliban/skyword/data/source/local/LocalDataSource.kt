package anliban.skyword.data.source.local

import android.content.Context
import anliban.skyword.data.User

interface ILocalDataSource {

    var fcmToken: String?
    var email: String?
    var password: String?
    val user: User
}

class LocalDataSource(private val pref: PrefModel) : ILocalDataSource {

    override var fcmToken: String?
        get() = pref.fcmToken
        set(value) {
            pref.fcmToken = value
        }

    override var email: String?
        get() = pref.email
        set(value) {
            pref.email = value
        }

    override var password: String?
        get() = pref.password
        set(value) {
            pref.password = value
        }

    override val user: User
        get() = User(pref.email!!, pref.password!!)
}

class PrefModel(context: Context) : PreferenceModel(context) {
    var fcmToken by stringPreference("fcm_token")
    var email by stringPreference("email", User.INVALID_EMAIL)
    var password by stringPreference("password", User.INVALID_PASSWORD)
}