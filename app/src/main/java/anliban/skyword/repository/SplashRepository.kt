package anliban.skyword.repository

import anliban.skyword.data.Result
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.data.source.local.ILocalDataSource
import anliban.skyword.data.source.remote.IFirebaseAuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ISplashRepository {

    suspend fun login(): Result<User>
}

class SplashRepository(
    private val firebaseAuthDataSource: IFirebaseAuthDataSource,
    private val localDataSource: ILocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISplashRepository {

    override suspend fun login() = withContext(dispatcher) {
        val user = localDataSource.user
        if (!user.isLogin()) {
            return@withContext Result.error(Exception())
        }
        val authResult = firebaseAuthDataSource.login(user)
        if (authResult.status == Status.ERROR) {
            return@withContext Result.error(authResult.error!!.e)
        }
        return@withContext Result.success(user)
    }
}