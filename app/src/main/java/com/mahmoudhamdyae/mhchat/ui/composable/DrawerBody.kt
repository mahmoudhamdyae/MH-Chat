package com.mahmoudhamdyae.mhchat.ui.composable

import android.view.MenuItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerBody(
    menuItems: List<MenuItem>,
//    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onItemClick: (MenuItem) -> Unit,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(menuItems) { item ->
            DrawerItem(
                menuItem = item,
                modifier = modifier
            ) {
                scope.launch {
//                    scaffoldState.drawerState.close()
                }
                onItemClick(item)
            }
        }
    }
}