package com.test.henripotier.ui.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.test.henripotier.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("app:priceText")
fun AppCompatTextView.setPriceText(
    price: Int,
) {
    text = context.getString(R.string.price, price)
}



@BindingAdapter("app:coverImage")
fun AppCompatImageView.setCoverImage(
    cover: String?,
) {
    Glide.with(context).load(cover)
        .into(this)

}


