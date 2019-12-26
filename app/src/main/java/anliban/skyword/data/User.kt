package anliban.skyword.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val email: String = INVALID_EMAIL,
    @Exclude val password: String = INVALID_PASSWORD,
    val token: String = "",
    val reward: Int = 0
) {
    @Exclude
    fun isLogin() =
        email != INVALID_EMAIL && password != INVALID_PASSWORD

    @Exclude
    fun convertedEmail() = email.replace(".", "*")

    @Exclude
    fun toMap() =
        mapOf("email" to email, "token" to token, "reward" to reward)

    companion object {
        const val INVALID_EMAIL = "email null!!!!!!!!!!"
        const val INVALID_PASSWORD = "password null!!!!!!!!!!"
    }
}