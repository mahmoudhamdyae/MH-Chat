package com.mahmoudhamdyae.mhchat.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "empty", modifier = modifier.fillMaxSize())
}