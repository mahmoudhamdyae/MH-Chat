package com.mahmoudhamdyae.mhchat.ui.composable

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mahmoudhamdyae.mhchat.R

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    photoUri: Uri? = null,
) {
    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(data = photoUri)
            .build(),
        placeholder = painterResource(R.drawable.loading_img),
        error = painterResource(R.drawable.default_image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
            .clip(CircleShape)
    )
}