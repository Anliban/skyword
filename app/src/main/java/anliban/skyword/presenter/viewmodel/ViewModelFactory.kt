package anliban.skyword.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import anliban.skyword.repository.IJoinRepository
import anliban.skyword.repository.ILoginRepository
import anliban.skyword.repository.ISendMessageRepository
import anliban.skyword.repository.ISplashRepository

class SplashViewModelFactory(
    private val repository: ISplashRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return SplashViewModel(repository) as T
    }
}

class LoginViewModelFactory(
    private val repository: ILoginRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return LoginViewModel(repository) as T
    }
}

class JoinViewModelFactory(
    private val repository: IJoinRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return JoinViewModel(repository) as T
    }
}

class SendMessageViewModelFactory(
    private val repository: ISendMessageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return SendMessageViewModel(repository) as T
    }
}