package com.test.henripotier.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.henripotier.data.dao.BookDao
import com.test.henripotier.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = true)
abstract class HPDataBase : RoomDatabase() {

    abstract fun getBookDao(): BookDao

    companion object {
        const val DB_NAME = "henripotier_db.db"
    }
}