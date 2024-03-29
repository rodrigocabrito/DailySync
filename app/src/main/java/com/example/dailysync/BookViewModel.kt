package com.example.dailysync

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.ReadingSession
import com.example.dailysync.bookModels.Status
import com.example.dailysync.bookRepository.BookRepository
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.toLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    var emptySearchedResult: Boolean = false
    var showRemovingDialog: Boolean = false

    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isNextItemsLoading: MutableState<Boolean> = mutableStateOf(false)

    private var _items: MutableState<List<Items>> = mutableStateOf(emptyList())
    val items: State<List<Items>> = _items

    val errorType: MutableState<Internet.ErrorType> = mutableStateOf(Internet.ErrorType.NONE)

    fun clearLoadItemsList() {
        _items.value = emptyList()
    }

    fun loadItems(query: String, startIndex: Int) {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000)
            repository.getBooksList(query, startIndex) { result ->
                result.onSuccess {
                    result.toLiveData {
                        if (data?.totalItems != 0) {
                            if(emptySearchedResult)
                                data?.items = emptyList()

                            data?.items?.let {
                                _items.value = _items.value + it
                                errorType.value = Internet.ErrorType.NONE
                            }
                        } else {
                            errorType.value = Internet.ErrorType.CUSTOM
                        }
                    }
                }.onError {

                }.onException {
                    handleException(exception)
                }
                isLoading.value = false
            }
        }
    }


    fun loadNextItems(query: String, startIndex: Int) {
        viewModelScope.launch {
            if (!isNextItemsLoading.value && (startIndex + 1) % 20 == 0) {
                isNextItemsLoading.value = true
                delay(1000)
                repository.getBooksList(query, startIndex + 1) { result ->
                    result.onSuccess {
                        this.data?.items?.let {
                            _items.value = _items.value + it
                        }
                    }.onError {

                    }.onException {
                        handleException(exception)
                    }
                    isNextItemsLoading.value = false
                }
            }
        }
    }

    fun getItemsByStatus(status: Status, callback: (List<Items>) -> Unit) {
        viewModelScope.launch {
            repository.getItemsByStatus(status, callback)
        }
    }

    fun updateStatus(item: Items) {
        viewModelScope.launch {
            if (repository.checkItemExists(item.id)) {
                repository.updateItem(item)
            } else {
                repository.insertItem(item)
            }
        }
    }


    fun deleteItem(id: String, navController: NavHostController) {
        viewModelScope.launch {
            showRemovingDialog = true
            delay(500)
            repository.deleteItemById(id)
            repository.deleteReadingSessionsByItemId(id)
            showRemovingDialog = false
            // Navigation logic if needed
            navController.popBackStack()
        }
    }

    private fun handleException(exception: Throwable) {
        if (!Internet.isAvailable()) {
            errorType.value = Internet.ErrorType.INTERNET
        } else {
            exception.message?.let {
                errorType.value = Internet.ErrorType.EXCEPTION
            }
        }
    }

    fun registerReadingSession(item: Items, currentPage: Int, durationMinutes: Int, date: Long) {
        val readingSession = ReadingSession(item.id, currentPage, durationMinutes, date)
        insertReadingSession(item, readingSession)
        updateStatus(item)
    }

    private fun insertReadingSession(item: Items,readingSession: ReadingSession) {
        viewModelScope.launch {
            repository.insertReadingSession(item, readingSession)
        }
    }
}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(repository) as T
    }
}

