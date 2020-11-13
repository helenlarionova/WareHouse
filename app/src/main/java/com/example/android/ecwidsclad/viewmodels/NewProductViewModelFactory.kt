package com.example.android.ecwidsclad.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.ecwidsclad.database.ProductDatabaseDao

class NewProductViewModelFactory (
    private val dataSource: ProductDatabaseDao,
    private val chosenProductId: Long): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewProductViewModel::class.java)) {
            return NewProductViewModel(
                dataSource,
                chosenProductId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}