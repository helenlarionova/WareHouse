package com.example.android.ecwidsclad.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.example.android.ecwidsclad.R
import com.example.android.ecwidsclad.database.ProductDatabase
import com.example.android.ecwidsclad.databinding.AllProductsFragmentBinding
import com.example.android.ecwidsclad.viewmodels.AllProductsViewModel
import com.example.android.ecwidsclad.viewmodels.AllProductsViewModelFactory

class AllProductsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AllProductsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.all_products_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ProductDatabase.getInstance(application).databaseDao

        val viewModelFactory =
            AllProductsViewModelFactory(
                dataSource,
                application
            )

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(AllProductsViewModel::class.java)

        binding.allProductsViewModel = viewModel

        binding.setLifecycleOwner(this)

        binding.fab.setOnClickListener {view: View ->
            Navigation.findNavController(view).navigate(R.id.action_allProductsFragment_to_newProductFragment)
        }

        val adapter = ProductsAdapter(
            ProductListener { productId ->
                viewModel.onProductItemClicked(productId)
            })
        binding.productsList.adapter = adapter

        viewModel.navigateToNewProductFragment.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    AllProductsFragmentDirections.actionAllProductsFragmentToNewProductFragment(
                        it
                    )
                )
                viewModel.onNewProductNavigated()
            }

        })

        viewModel.products.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}
