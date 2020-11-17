package com.example.android.ecwidsclad.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.ecwidsclad.database.Product
import com.example.android.ecwidsclad.database.ProductDatabaseDao
import kotlinx.coroutines.*

class NewProductViewModel (val database: ProductDatabaseDao, val chosenProductId: Long) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _productLiveData = MutableLiveData<Product>()
    val productLiveData:LiveData<Product> = _productLiveData

    //private var productIsEdit: Boolean = false
    private var _productIsEdit = MutableLiveData<Boolean>()
    val productIsEdit: LiveData<Boolean> = _productIsEdit


    private val _permissionRequest = MutableLiveData<Boolean>()
    var permissionRequest:LiveData<Boolean> = _permissionRequest

    fun permissionRequestClick(){
        _permissionRequest.value = true
    }

    init {
            if (chosenProductId != 0L) {
                //This is edit case
                getChosenProduct(chosenProductId)
                _productIsEdit.value = true
            }

            else {
                /*This is for adding a new product. We initialize a Product with default or null values
               This is because two-way databinding in the NewProductFragment is designed to
               register changes automatically, but it will need a product object to register those changes.*/
                _productLiveData.value = Product()
                _productIsEdit.value = false
            }
        _permissionRequest.value = false

    }


    private val _navigationToAllProducts = MutableLiveData<Boolean>()
    var navigationToAllProducts: LiveData<Boolean> = _navigationToAllProducts

    fun doneNavigating() {
        _navigationToAllProducts.value = null
    }




//    private val _permissionRequest = MutableLiveData<String>()
//    val permissionRequest:LiveData<String> = _permissionRequest
//
//    fun permissionRequestClick(){
//        _permissionRequest.value = "true"
//    }

    fun onPermissionResult(data: Uri? = null, granted: Boolean) {
        if (granted){
            data?.let {
                _productLiveData.value?.photoUrl = it.toString()
                _productLiveData.value?.notifyChange()

            }
        }
        _permissionRequest.value = false
    }


    fun onSave(){
        uiScope.launch {
            _productLiveData.value?.let {
                if (!_productIsEdit.value!!)
                    insert(it)
                else
                    update(it)
            }
        }
        _navigationToAllProducts.value = true
    }

    fun onDelete(){
        uiScope.launch {
            _productLiveData.value?.let {
                delete(it)
            }
        }
        _navigationToAllProducts.value = true
    }

    private suspend fun insert(product: Product) {
        withContext(Dispatchers.IO) {
            database.insert(product)
        }
    }

    private suspend fun update(product: Product) {
        withContext(Dispatchers.IO){
            database.update(product)
        }
    }

    private suspend fun delete(product: Product) {
        withContext(Dispatchers.IO) {
            database.delete(product)
        }
    }

    private suspend fun getProductFromDatabase(productId: Long) : Product? {
        var product = withContext(Dispatchers.IO) {
            database.get(productId)
        }
        return product
    }


    private fun getChosenProduct(productId: Long){
        uiScope.launch {
            _productLiveData.postValue(getProductFromDatabase(productId))
        }

    }
}
