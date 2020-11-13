package com.example.android.ecwidsclad.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.ecwidsclad.R
import com.example.android.ecwidsclad.database.ProductDatabase
import com.example.android.ecwidsclad.databinding.NewProductFragmentBinding
import com.example.android.ecwidsclad.newproduct.NewProductFragmentArgs
import com.example.android.ecwidsclad.newproduct.NewProductFragmentDirections
import com.example.android.ecwidsclad.viewmodels.NewProductViewModel
import com.example.android.ecwidsclad.viewmodels.NewProductViewModelFactory

class NewProductFragment : Fragment() {

    private val permissionsRequestCode = 123
    val GALLERY_REQUEST = 1
    lateinit var viewModel: NewProductViewModel
    private lateinit var binding: NewProductFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.new_product_fragment, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = ProductDatabase.getInstance(application).databaseDao

        val chosenProductId = NewProductFragmentArgs.fromBundle(
            requireArguments()
        ).productId


        val viewModelFactory =
            NewProductViewModelFactory(
                dataSource,
                chosenProductId
            )

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewProductViewModel::class.java)

        binding.newProductViewModel = viewModel

        binding.setLifecycleOwner(this)

        viewModel.navigationToAllProducts.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(NewProductFragmentDirections.actionNewProductFragmentToAllProductsFragment())
                viewModel.doneNavigating()
                hideKeyboard()


            }
        })

        viewModel.permissionRequest.observe(viewLifecycleOwner, Observer {
            if (it){
                when {
                    permissionGranted() -> openGallery()
                    shouldShowRationale() -> {
                    }// Show the rationale UI and then request permission
                    else -> requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        permissionsRequestCode
                    )
                }
            }

        })
        binding.fabSave.setOnClickListener{
            saveProduct()
        }


        return binding.root
    }

    private fun saveProduct() {
        if (viewModel.productLiveData.value?.title.isNullOrBlank() ||
            viewModel.productLiveData.value?.price.isNullOrBlank() ||
            viewModel.productLiveData.value?.photoUrl.isNullOrBlank()){
            Toast.makeText(context, R.string.product_name_warning, Toast.LENGTH_SHORT).show()
            return
        }else{
            viewModel.onSave()
        }
    }

    fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)

        photoPickerIntent.type = "image/*"
        this.startActivityForResult(
            photoPickerIntent,
            GALLERY_REQUEST
        )
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GALLERY_REQUEST ->
                if (resultCode == -1){
                    val selectedImageUri: Uri = data?.getData()!!
                    viewModel.onPermissionResult(selectedImageUri, true)

                }else{
                    viewModel.onPermissionResult(granted = false)
                }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == permissionsRequestCode) {
            if (permissions[0]  == android.Manifest.permission.READ_EXTERNAL_STORAGE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, do your work
            }
        }
    }

    private fun shouldShowRationale() = ActivityCompat.shouldShowRequestPermissionRationale(
        requireActivity(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED


}
