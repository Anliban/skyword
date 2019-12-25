package anliban.skyword.repository

import anliban.skyword.data.Resource
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.data.source.local.ILocalDataSource
import anliban.skyword.data.source.remote.IFirebaseAuthDataSource
import anliban.skyword.data.source.remote.IFirebaseDbDataSource
import anliban.skyword.data.source.remote.IFirebaseInstanceDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

interface ISplashRepository {

    suspend fun attemptLogin(): Resource<User>
}

class SplashRepository(
    private val firebaseInstanceDataSource: IFirebaseInstanceDataSource,
    private val firebaseAuthDataSource: IFirebaseAuthDataSource,
    private val firebaseDbDataSource: IFirebaseDbDataSource,
    private val localDataSource: ILocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISplashRepository {

    override suspend fun attemptLogin() = withContext(dispatcher) {
        val tokenResult = firebaseInstanceDataSource.getToken()
        if (tokenResult.status == Status.ERROR) {
            return@withContext Resource.error(tokenResult.error!!.e)
        }
        localDataSource.fcmToken = tokenResult.data
        val user = localDataSource.user
        if (!user.isLogin()) {
            return@withContext Resource.error(Exception())
        }
        val authResult = firebaseAuthDataSource.login(user.email, user.password)
        if (authResult.status == Status.ERROR) {
            return@withContext Resource.error(authResult.error!!.e)
        }
        firebaseDbDataSource.saveUser(user)
        return@withContext Resource.success(user)
    }
}