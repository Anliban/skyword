package anliban.skyword.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.repository.IJoinRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class JoinViewModel(private val repository: IJoinRepository) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val _success = MutableLiveData<User>()
    val success: LiveData<User> = _success
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun join(user: User) {
        scope.launch {
            val result = repository.join(user)
            if (result.status == Status.ERROR) {
                val msg = when (result.error?.e) {
                    is FirebaseAuthWeakPasswordException -> "비밀번호는 6자 이상입니다."
                    is FirebaseAuthInvalidCredentialsException -> "아이디형식이 잘못됐습니다."
                    is FirebaseAuthUserCollisionException -> "이미 가입된 아이디가 존재합니다."
                    else -> "회원가입에 실패했습니다."
                }
                _message.value = msg
            } else {
                _success.value = result.data
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}