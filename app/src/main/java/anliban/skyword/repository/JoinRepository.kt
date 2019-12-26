package anliban.skyword.repository

import anliban.skyword.data.Result
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.data.source.local.ILocalDataSource
import anliban.skyword.data.source.remote.IFirebaseAuthDataSource
import anliban.skyword.data.source.remote.IFirebaseDbDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IJoinRepository {

    suspend fun join(user: User): Result<User>
}

class JoinRepository(
    private val authDataSource: IFirebaseAuthDataSource,
    private val dbDataSource: IFirebaseDbDataSource,
    private val localDataSource: ILocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IJoinRepository {

    override suspend fun join(user: User) = withContext(dispatcher) {
        val authResult = authDataSource.createUser(user)
        if (authResult.status == Status.ERROR) {
            return@withContext Result.error(authResult.error?.e)
        }
        val dbResult = dbDataSource.saveUser(
            User(user.email, user.password, localDataSource.fcmToken)
        )
        if (dbResult.status == Status.ERROR) {
            return@withContext Result.error(dbResult.error?.e)
        }
        localDataSource.email = user.email
        localDataSource.password = user.password
        return@withContext Result.success(user)
    }
}