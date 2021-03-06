package com.test.henripotier.ui.bindingUtil

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.test.henripotier.R


@BindingAdapter("app:priceText")
fun AppCompatTextView.setPriceText(
    price: Int,
) {
    text = context.getString(R.string.price, price.toString())
}



@BindingAdapter("app:coverImage")
fun AppCompatImageView.setCoverImage(
    cover: String?,
) {
    Glide.with(context).load(cover)
        .into(this)

}


