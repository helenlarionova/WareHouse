package com.example.android.ecwidsclad.database

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class Product(

    @PrimaryKey(autoGenerate = true)
    var productId: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String = "",

    @ColumnInfo(name = "price")
    var price: String = ""


) : BaseObservable()