package com.mahmoudhamdyae.mhchat.ui.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun BasicToolBar(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    canNavigate: Boolean = false,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(stringResource(id = title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigate) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun ActionToolbar(
    @StringRes title: Int,
    endActionIcons: List<ImageVector>,
    endActions: List<() -> Unit>,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        actions = {
            Row(modifier) {
                endActionIcons.zip(endActions) { endActionIcon, endAction ->
                    IconButton(onClick = endAction) {
                        Icon(imageVector = endActionIcon, contentDescription = null)
                    }
                }
            }
        },
        modifier = modifier
    )
}