package com.example.android.ecwidsclad.ui


import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.example.android.ecwidsclad.R
import com.example.android.ecwidsclad.database.ProductDatabase
import com.example.android.ecwidsclad.viewmodels.AllProductsViewModel
import com.example.android.ecwidsclad.viewmodels.AllProductsViewModelFactory

class AllProductsFragment : Fragment(), SearchView.OnQueryTextListener {

    lateinit var viewModel: AllProductsViewModel

    var mSearchQuery: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState != null){
            mSearchQuery = savedInstanceState.getString("searchQuery");
        }

        val binding: com.example.android.ecwidsclad.databinding.AllProductsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.all_products_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ProductDatabase.getInstance(application).databaseDao

        val viewModelFactory =
            AllProductsViewModelFactory(
                dataSource,
                application
            )

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AllProductsViewModel::class.java)

        binding.allProductsViewModel = viewModel

        binding.setLifecycleOwner(this)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)


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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_products_list, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        if(!mSearchQuery.isNullOrEmpty()){
            searchView.setIconified(false);
            searchItem.expandActionView()
            searchView.setQuery(mSearchQuery, false)
            searchView.clearFocus()
        }else {
            searchView.setIconifiedByDefault(false)
            searchView.requestFocus()
        }

        searchView.setOnQueryTextListener(this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.setFilter(it)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            mSearchQuery = query
            viewModel.setFilter(it)
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchQuery", mSearchQuery);
        super.onSaveInstanceState(outState)
    }




}
