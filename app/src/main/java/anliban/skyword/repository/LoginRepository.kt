package anliban.skyword.repository

import anliban.skyword.data.Result
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.data.source.local.ILocalDataSource
import anliban.skyword.data.source.remote.IFirebaseAuthDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ILoginRepository {

    suspend fun login(user: User): Result<User>
}

class LoginRepository(
    private val authDataSource: IFirebaseAuthDataSource,
    private val localDataSource: ILocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILoginRepository {

    override suspend fun login(user: User): Result<User> {
        val authResult = authDataSource.login(user)
        if (authResult.status == Status.ERROR) {
            return Result.error(authResult.error?.e)
        }
        localDataSource.email = user.email
        localDataSource.password = user.password
        return Result.success(user)
    }
}