package com.mahmoudhamdyae.mhchat.ui.screens.notifications

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object NotificationsDestination: NavigationDestination {
    override val route: String = "notifications"
    override val titleRes: Int = R.string.home_screen_title
}

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel(),
) {
    Scaffold(modifier = modifier) { innerPadding ->
        NotificationsContent(modifier.padding(innerPadding))
    }
}

@Composable
fun NotificationsContent(modifier: Modifier = Modifier) {
}

@Composable
fun NotificationsList(modifier: Modifier = Modifier) {
}

@Composable
fun NotificationListItem(modifier: Modifier = Modifier) {
}