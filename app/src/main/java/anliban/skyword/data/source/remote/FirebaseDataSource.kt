package anliban.skyword.data.source.remote

import anliban.skyword.data.Resource
import anliban.skyword.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface IFirebaseInstanceDataSource {

    suspend fun getToken(): Resource<String>
}

class FirebaseInstanceDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFirebaseInstanceDataSource {

    override suspend fun getToken() = withContext(dispatcher) {
        return@withContext suspendCoroutine<Resource<String>> { continuation ->
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val token = it.result!!.token
                        continuation.resume(Resource.success(token))
                    } else {
                        continuation.resume(Resource.error(it.exception))
                    }
                }
        }
    }
}

interface IFirebaseAuthDataSource {

    suspend fun createUser(email: String, password: String): Resource<FirebaseUser>

    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun logout()
}

class FirebaseAuthDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFirebaseAuthDataSource {

    private val auth = FirebaseAuth.getInstance()

    override suspend fun createUser(email: String, password: String) = withContext(dispatcher) {
        return@withContext suspendCoroutine<Resource<FirebaseUser>> { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val user = it.result!!.user
                        if (user != null) {
                            continuation.resume(Resource.success(user))
                        } else {
                            continuation.resume(Resource.error(it.exception))
                        }
                    } else {
                        continuation.resume(Resource.error(it.exception))
                    }
                }
        }
    }

    override suspend fun login(email: String, password: String) = withContext(dispatcher) {
        return@withContext suspendCoroutine<Resource<FirebaseUser>> { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val user = it.result!!.user
                        if (user != null) {
                            continuation.resume(Resource.success(user))
                        } else {
                            continuation.resume(Resource.error(it.exception))
                        }
                    } else {
                        continuation.resume(Resource.error(it.exception))
                    }
                }
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}

interface IFirebaseDbDataSource {

    suspend fun saveUser(user: User)
}

class FirebaseDbDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFirebaseDbDataSource {

    private val database = FirebaseDatabase.getInstance().reference

    override suspend fun saveUser(user: User) {
        val params = mapOf("/users/${user.email}" to user.token)
        database.updateChildren(params)
    }
}