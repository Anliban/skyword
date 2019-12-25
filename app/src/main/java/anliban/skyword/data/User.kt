package anliban.skyword.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val email: String = INVALID_EMAIL,
    @Exclude val password: String = INVALID_PASSWORD,
    val reward: Int = 0
) {
    @Exclude
    fun isLogin() =
        email != INVALID_EMAIL && password != INVALID_PASSWORD

    fun convertedEmail() = email.replace(".", "*")

    companion object {
        const val INVALID_EMAIL = "email null!!!!!!!!!!"
        const val INVALID_PASSWORD = "password null!!!!!!!!!!"
    }
}