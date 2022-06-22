package com.test.henripotier.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.henripotier.data.model.Book
import com.test.henripotier.repository.api.BookRepository
import com.test.henripotier.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 10, enablePlaceholders = false)
    }

    private var _booksFromRemoteLiveData = MutableLiveData<Response<Nothing>>()
    var booksFromRemoteLiveData: LiveData<Response<Nothing>> = _booksFromRemoteLiveData


    var mAllBooksFlow: Flow<PagingData<Book>>? = null
    val mCart = mutableSetOf<String>()

    init {
        getBooksRemotely()
        fetchBooks()
    }

    private fun getBooksRemotely() {
        viewModelScope.launch(Dispatchers.IO) {
            _booksFromRemoteLiveData.postValue(Response.Loading)
            val result = bookRepository.getBooksRemotely()
            _booksFromRemoteLiveData.postValue(result)
        }
    }


    private fun fetchBooks() {
        val pagingSourceFactory = {
            bookRepository.getLocalBooks()
        }

        mAllBooksFlow = Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory
        ).flow.cachedIn(viewModelScope)

    }

    fun addToCart(isbn: String): Boolean {
        return if (mCart.contains(isbn)) {
            false
        } else {
            mCart.add(isbn)
            true
        }
    }

    fun clearCart() {
        mCart.clear()
    }


}