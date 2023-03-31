package com.mahmoudhamdyae.mhchat.common.ext

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

fun Bitmap?.toImageUri(context: Context): Uri? {
    if (this == null) return null

    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, this, "Title", null)
    return Uri.parse(path.toString())
}