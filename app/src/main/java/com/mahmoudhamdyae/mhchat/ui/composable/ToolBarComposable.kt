package com.mahmoudhamdyae.mhchat.ui.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicToolBar(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    canNavigateUp: Boolean = false,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )},
        modifier = modifier.background(MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if (canNavigateUp) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
        scrollBehavior = null,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionToolbar(
    @StringRes title: Int,
    endActionIcons: List<ImageVector>,
    endActions: List<() -> Unit>,
    modifier: Modifier = Modifier,
    canNavigateUp: Boolean = false,
    navigateUp: () -> Unit = {},
    onMainScreen: Boolean = false,
    openDrawer: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            ) },
        actions = {
            Row(modifier) {
                endActionIcons.zip(endActions) { endActionIcon, endAction ->
                    IconButton(onClick = endAction) {
                        Icon(imageVector = endActionIcon, contentDescription = null)
                    }
                }
            }
        },
        navigationIcon = {
            if (canNavigateUp) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            } else if (onMainScreen) {
                IconButton(onClick = openDrawer) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                }
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
    )
}