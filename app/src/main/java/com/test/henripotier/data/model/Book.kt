package com.test.henripotier.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * This class defines the entity Book received by the server
 */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    val isbn : String,
    @ColumnInfo(name = "title")
    val title : String,
    @ColumnInfo(name = "price")
    val price : Int,
    @ColumnInfo(name = "cover")
    val cover : String?,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (isbn != other.isbn) return false
        if (title != other.title) return false
        if (price != other.price) return false
        if (cover != other.cover) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isbn.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + price
        result = 31 * result + (cover?.hashCode() ?: 0)
        return result
    }
}


