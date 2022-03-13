package com.drax.sendit.view.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.drax.sendit.R

@BindingAdapter("img")
fun ImageView.loadImageFromUri(imageUrl: String){
    Glide.with(this)
        .load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_baseline_phonelink_64)
        .into(this)
}