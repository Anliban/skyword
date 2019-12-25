package anliban.skyword.data

data class User(
    val email: String = INVALID_EMAIL,
    val password: String = INVALID_PASSWORD,
    val token: String?
) {
    fun isLogin() =
        email != INVALID_EMAIL && password != INVALID_PASSWORD && token != null

    companion object {
        const val INVALID_EMAIL = "email null!!!!!!!!!!"
        const val INVALID_PASSWORD = "password null!!!!!!!!!!"
    }
}