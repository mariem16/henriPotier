package com.test.henripotier.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.henripotier.data.model.Book

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<Book>)

    @Query("SELECT * FROM books")
    fun getPagedBook(): PagingSource<Int, Book>

    @Query("SELECT * FROM books WHERE isbn IN (:isbns) ")
    suspend fun getBooksByIsbn(isbns : List<String>): List<Book>?

}