package anliban.skyword.repository

import anliban.skyword.data.Result
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.data.source.local.ILocalDataSource
import anliban.skyword.data.source.remote.IFirebaseDbDataSource
import anliban.skyword.data.source.remote.IFirebaseInstanceDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception

interface ISendMessageRepository {

    suspend fun sync(): Result<Nothing?>
}

class SendMessageRepository(
    private val instanceDataSource: IFirebaseInstanceDataSource,
    private val dbDataSource: IFirebaseDbDataSource,
    private val localDataSource: ILocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISendMessageRepository {

    override suspend fun sync() = withContext(dispatcher) {
        val localUser = localDataSource.user
        if (!localUser.isLogin()) {
            return@withContext Result.error(Exception())
        }
        val userResult = dbDataSource.getUser(localUser)
        if (userResult.status == Status.ERROR) {
            return@withContext Result.error(userResult.error?.e)
        }
        if (userResult.data == null) {
            return@withContext Result.error(Exception())
        }
        val user = userResult.data
        if (isUpdateFcmToken(user.token)) {
            async { registerFcmToken(user) }
        }
        return@withContext Result.success(null)
    }

    private suspend fun registerFcmToken(user: User) {
        val tokenResult = instanceDataSource.getToken()
        if (tokenResult.status == Status.SUCCESS) {
            val token = tokenResult.data!!
            localDataSource.fcmToken = token
            val updateUser = User(user.email, user.password, token)
            dbDataSource.updateUser(updateUser)
        }
    }

    private fun isUpdateFcmToken(token: String) =
        token.isEmpty() || token != localDataSource.fcmToken
}