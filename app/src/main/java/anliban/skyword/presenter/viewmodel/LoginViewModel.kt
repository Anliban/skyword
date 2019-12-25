package anliban.skyword.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.repository.ILoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: ILoginRepository) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val _login = MutableLiveData<User>()
    val login: LiveData<User> = _login
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun login(user: User) {
        scope.launch {
            val result = repository.login(user)
            when (result.status) {
                Status.SUCCESS -> _login.value = result.data
                Status.ERROR -> _message.value = "로그인 실패."
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}