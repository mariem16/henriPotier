package com.test.henripotier.repository

import androidx.paging.PagingSource
import com.test.henripotier.data.dao.BookDao
import com.test.henripotier.data.model.Book
import com.test.henripotier.api.ApiService
import com.test.henripotier.utils.Response

class BookRepository(private val apiService: ApiService, private val bookDao : BookDao) {

    suspend fun getBooksRemotely(): Response<Nothing> {
        val result = apiService.getBooks()
        return if(result.isSuccessful){
            val books = result.body() as List<Book>
            bookDao.insertAll(books)
            Response.Success()
        }else {
            Response.Error(result.errorBody().toString())
        }
    }

    fun getLocalBooks(): PagingSource<Int, Book> {
         return bookDao.getPagedBook()
    }

    suspend fun getBooksByIsbn(isbns: List<String>) = bookDao.getBooksByIsbn(isbns)

}