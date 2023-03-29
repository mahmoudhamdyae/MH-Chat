package com.mahmoudhamdyae.mhchat.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object SettingsDestination: NavigationDestination {
    override val route: String = "settings"
    override val titleRes: Int = R.string.settings_screen_title
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {

    BasicToolBar(title = SettingsDestination.titleRes)
}