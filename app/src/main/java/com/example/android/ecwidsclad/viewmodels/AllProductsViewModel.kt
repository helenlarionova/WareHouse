package com.example.android.ecwidsclad.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.ecwidsclad.database.ProductDatabaseDao

class AllProductsViewModel (database: ProductDatabaseDao, application: Application) : ViewModel() {

    val products = database.getAllProducts()

//    private val _navigateToNewProductFragment = MutableLiveData<Product>()
//    val navigateToNewProductFragment: LiveData<Product> = _navigateToNewProductFragment
//
//    fun doneNavigation(){
//        _navigateToNewProductFragment.value = null
//    }

    private val _navigateToNewProductFragment = MutableLiveData<Long>()
    val navigateToNewProductFragment:LiveData<Long> = _navigateToNewProductFragment

    fun onProductItemClicked(id: Long){
        _navigateToNewProductFragment.value = id
    }

    fun onNewProductNavigated(){
        _navigateToNewProductFragment.value = null
    }


}
