package anliban.skyword.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import anliban.skyword.repository.ISplashRepository

class SplashViewModelFactory(
    private val repository: ISplashRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return SplashViewModel(repository) as T
    }
}