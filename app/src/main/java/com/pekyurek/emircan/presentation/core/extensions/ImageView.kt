package com.pekyurek.emircan.presentation.core.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide


fun ImageView.loadImageFromUrl(url: String?, @DrawableRes errorDrawableResId: Int? = null) {
    Glide.with(context).load(url).error(errorDrawableResId).into(this)
}