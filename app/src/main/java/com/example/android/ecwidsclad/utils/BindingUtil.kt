package com.example.android.ecwidsclad.utils

import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.android.ecwidsclad.database.Product


@BindingAdapter("productTitle")
fun TextView.setTitleText(item: Product?){
    item?.let {
        text = item.title
    }

}

@BindingAdapter("productPrice")
fun TextView.setPriceText(item: Product?){
    item?.let {
        text = item.price.toString() + " руб."
    }
}


@BindingAdapter("productImage")
fun ImageView.setImageUrl(item: Product?){
    item?.let {
        if (it.photoUrl.isNotEmpty()) setImageURI(Uri.parse(item.photoUrl)) else setImageResource(android.R.drawable.ic_menu_gallery)
    }
}



