package com.example.android.ecwidsclad.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecwidsclad.database.Product
import com.example.android.ecwidsclad.databinding.ProductItemViewBinding

class ProductsAdapter (val clickListener: ProductListener) : ListAdapter<Product, ProductsAdapter.ViewHolder>(
    ProductDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(clickListener, productItem)
    }

    class ViewHolder private constructor(val binding: ProductItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ProductListener, item: Product) {
            binding.product = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductItemViewBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(
                    binding
                )
            }
        }

    }
}

    class ProductDiffCallback:
            DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    class ProductListener(val clickListener: (productId: Long) -> Unit) {
        fun onClick(product: Product) = clickListener(product.productId)
    }



