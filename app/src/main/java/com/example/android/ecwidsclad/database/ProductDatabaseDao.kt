package com.example.android.ecwidsclad.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDatabaseDao {

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM products_table WHERE productId = :key")
    fun get(key: Long): Product?

    @Query("DELETE FROM products_table")
    fun clear()

    @Query("SELECT * FROM products_table ORDER BY productId DESC")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * from products_table WHERE title LIKE :filter ORDER BY productId")
    fun getProductsFiltered(filter: String): LiveData<List<Product>>

}