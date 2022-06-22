package com.test.henripotier.repository

import androidx.paging.PagingSource
import com.test.henripotier.api.ApiService
import com.test.henripotier.data.dao.BookDao
import com.test.henripotier.data.model.Book
import com.test.henripotier.utils.Response
import java.net.UnknownHostException

class BookRepository(private val apiService: ApiService, private val bookDao: BookDao) {

    suspend fun getBooksRemotely(): Response<Nothing> {
        return try {
            val result = apiService.getBooks()
            if (result.isSuccessful) {
                val books = result.body() as List<Book>
                bookDao.insertAll(books)
                Response.Success()
            } else {
                Response.Error(result.errorBody().toString())
            }
        } catch (exception : UnknownHostException) {
            Response.Error("No internet connection!")
        } catch (exception: Exception) {
            Response.Error("error occurs when trying to fetch data!")
        }

    }

    fun getLocalBooks(): PagingSource<Int, Book> {
        return bookDao.getPagedBook()
    }

    suspend fun getBooksByIsbn(isbns: List<String>) = bookDao.getBooksByIsbn(isbns)

}