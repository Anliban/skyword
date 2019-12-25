package anliban.skyword.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import anliban.skyword.data.Status
import anliban.skyword.data.User
import anliban.skyword.repository.ISplashRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: ISplashRepository
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val _login = MutableLiveData<User>()
    val login: LiveData<User> = _login
    private val _loginFailure = MutableLiveData<Nothing>()
    val loginFailure: LiveData<Nothing> = _loginFailure

    fun login() {
        scope.launch {
            val result = repository.login()
            when (result.status) {
                Status.SUCCESS -> _login.value = result.data
                Status.ERROR -> _loginFailure.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}