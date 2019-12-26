package anliban.skyword.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import anliban.skyword.data.Status
import anliban.skyword.repository.ISendMessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SendMessageViewModel(private val repository: ISendMessageRepository) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val _syncResult = MutableLiveData<Boolean>()
    val syncResult: LiveData<Boolean> = _syncResult

    fun sync() {
        scope.launch {
            val result = repository.sync()
            _syncResult.value = result.status == Status.SUCCESS
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}