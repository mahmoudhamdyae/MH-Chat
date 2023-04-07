package com.mahmoudhamdyae.mhchat.ui.composable

import android.view.MenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahmoudhamdyae.mhchat.R

@Composable
fun DrawerItem(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit
) {
    Column(
        modifier = Modifier.clickable {
            onItemClick(menuItem)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 25.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        Divider()
    }
}
