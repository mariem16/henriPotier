package com.test.henripotier.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.test.henripotier.data.model.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): List<Book>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(books: Book)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(books: List<Book>)


    @Delete
    fun delete(book: Book)

    @Query("SELECT * FROM books")
    fun getPagedBook(): PagingSource<Int, Book>

    @Query("SELECT * FROM books WHERE isbn IN (:isbns) ")
    suspend fun getBooksByIsbn(isbns : List<String>): List<Book>?

}