package com.test.henripotier.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * This class defines the model Offer received by the server
 */
data class Offer(
    val sliceValue: Int,
    val type: String,
    val value: Int
)


