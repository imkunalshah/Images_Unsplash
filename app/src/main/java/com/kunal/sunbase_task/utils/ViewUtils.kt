package com.kunal.sunbase_task.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.kunal.sunbase_task.R

fun View.gone() {
    this.visibility = GONE
}

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.inVisible() {
    this.visibility = INVISIBLE
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.showNetworkUnavailableSnackBar(onDismiss: () -> Unit) {
    Snackbar.make(
        this,
        "Internet Unavailable",
        Snackbar.LENGTH_INDEFINITE
    ).setAction(this.context.resources.getString(R.string.dismiss)) {
        onDismiss.invoke()
    }.show()
}

fun Context.getDialogWidth(): Int {
    val displayMetrics = DisplayMetrics()
    (this as Activity?)?.windowManager
        ?.defaultDisplay
        ?.getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    return (width * 80) / 100
}

fun View.loadImage(
    imageUrl: String?,
    imageView: ImageView,
    onFailed: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null
) {
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this.context)
        .load(imageUrl)
        .apply(requestOptions)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onFailed?.invoke()
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke()
                return false
            }
        })
        .into(imageView)
}
