package anliban.skyword.data.source.remote

import anliban.skyword.data.Result
import anliban.skyword.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface IFirebaseInstanceDataSource {

    suspend fun getToken(): Result<String>
}

class FirebaseInstanceDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFirebaseInstanceDataSource {

    override suspend fun getToken() = withContext(dispatcher) {
        return@withContext suspendCoroutine<Result<String>> { continuation ->
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val token = it.result!!.token
                        continuation.resume(Result.success(token))
                    } else {
                        continuation.resume(Result.error(it.exception))
                    }
                }
        }
    }
}

interface IFirebaseAuthDataSource {

    suspend fun createUser(user: User): Result<FirebaseUser>

    suspend fun login(user: User): Result<FirebaseUser>

    suspend fun logout()
}

class FirebaseAuthDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFirebaseAuthDataSource {

    private val auth = FirebaseAuth.getInstance()

    override suspend fun createUser(user: User) = withContext(dispatcher) {
        return@withContext suspendCoroutine<Result<FirebaseUser>> { continuation ->
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    when {
                        it.isSuccessful && it.result?.user != null -> {
                            val firebaseUser = it.result?.user!!
                            continuation.resume(Result.success(firebaseUser))
                        }
                        else -> continuation.resume(Result.error(it.exception))
                    }
                }
        }
    }

    override suspend fun login(user: User) = withContext(dispatcher) {
        return@withContext suspendCoroutine<Result<FirebaseUser>> { continuation ->
            auth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    when {
                        it.isSuccessful && it.result?.user != null -> {
                            val firebaseUser = it.result?.user!!
                            continuation.resume(Result.success(firebaseUser))
                        }
                        else -> continuation.resume(Result.error(it.exception))
                    }
                }
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}

interface IFirebaseDbDataSource {

    suspend fun saveUser(user: User): Result<User>

    suspend fun getUser(user: User): Result<User?>

    suspend fun updateUser(user: User)
}

class FirebaseDbDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFirebaseDbDataSource {

    private val database = FirebaseDatabase.getInstance().reference

    override suspend fun saveUser(user: User) = withContext(dispatcher) {
        return@withContext suspendCoroutine<Result<User>> { continuation ->
            database.child("users")
                .child(user.convertedEmail())
                .setValue(user)
                .addOnCompleteListener {
                    when (it.isSuccessful) {
                        true -> continuation.resume(Result.success(user))
                        false -> continuation.resume(Result.error(it.exception))
                    }
                }
        }
    }

    override suspend fun getUser(user: User) = withContext(dispatcher) {
        return@withContext suspendCoroutine<Result<User?>> { continuation ->
            database.child("users")
                .child(user.convertedEmail())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        continuation.resume(Result.error(error.toException()))
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val firebaseUser = snapshot.getValue(User::class.java)
                        continuation.resume(Result.success(firebaseUser))
                    }
                })
        }
    }

    override suspend fun updateUser(user: User) {
        val userValues = user.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["users/${user.convertedEmail()}"] = userValues
        database.updateChildren(childUpdates)
    }
}