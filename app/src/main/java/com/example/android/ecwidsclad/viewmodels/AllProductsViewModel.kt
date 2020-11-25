package com.example.android.ecwidsclad.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.ecwidsclad.database.Product
import com.example.android.ecwidsclad.database.ProductDatabaseDao

class AllProductsViewModel (database: ProductDatabaseDao, application: Application) : ViewModel() {

    lateinit var products : LiveData<List<Product>>
    var filter = MutableLiveData<String>("%")
    init {
        products = Transformations.switchMap(filter) {filter ->
            database.getProductsFiltered(filter)
        }
    }

    fun setFilter(newFilter: String) {
        // optional: add wildcards to the filter
        val f = when {
            newFilter.isEmpty() -> "%"
            else -> "%$newFilter%"
        }
        filter.postValue(f) // apply the filter
    }

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

    fun filter(filterString: String) {
        products = Transformations.map(products) { it ->
            it.filter {
                it.title.contains(filterString)
            }
        }
    }



}
