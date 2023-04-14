package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.composable.ProfileImage
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.notifications.NotificationsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.profile.ProfileDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination

enum class DrawerElement(val icon: ImageVector, @StringRes val text: Int, val destination: String) {
    NONE(Icons.Default.Error, R.string.generic_error, ""),
    HOME(Icons.Default.Home, R.string.drawer_home, HomeDestination.route),
    Notifications(Icons.Default.Notifications, R.string.drawer_notifications, NotificationsDestination.route),
    PROFILE(Icons.Default.Person, R.string.drawer_profile, ProfileDestination.route),
    SETTINGS(Icons.Default.Settings, R.string.drawer_settings, SettingsDestination.route),
    LOGOUT(Icons.Default.Logout, R.string.drawer_log_out, LogInDestination.route)
}

@Composable
fun DrawerContent(
    currentUser: User,
    openScreen: (String) -> Unit,
    onSignOut: ((String) -> Unit) -> Unit,
    selectedItem: MutableState<DrawerElement>,
    closeDrawer: () -> Unit,
) {
    ModalDrawerSheet {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader(currentUser)
        DividerItem()
        DrawerItem(item = DrawerElement.HOME, openScreen, selectedItem, closeDrawer)
        DrawerItem(item = DrawerElement.Notifications, openScreen, selectedItem, closeDrawer)
        DrawerItem(item = DrawerElement.PROFILE, openScreen, selectedItem, closeDrawer)
        DrawerItem(item = DrawerElement.SETTINGS, openScreen, selectedItem, closeDrawer)
        DrawerItem(item = DrawerElement.LOGOUT, openScreen, selectedItem, closeDrawer, onSignOut = onSignOut)
    }
}

@Composable
private fun DrawerHeader(
    user: User,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(
            modifier = Modifier.size(120.dp),
            photoUri = user.imageUrl?.toUri(),
        )
        Text(modifier = Modifier.padding(top = 12.dp), text = user.userName, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(modifier = Modifier.padding(top = 8.dp, bottom = 30.dp), text = user.email, fontSize = 16.sp, fontWeight = FontWeight.Normal)
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
private fun DrawerItem(
    item: DrawerElement,
    openScreen: (String) -> Unit,
    selectedItem: MutableState<DrawerElement>,
    closeDrawer: () -> Unit,
    modifier: Modifier= Modifier,
    onSignOut: ((String) -> Unit) -> Unit = {},
) {
    NavigationDrawerItem(
        icon = { Icon(item.icon, contentDescription = null) },
        label = { Text(stringResource(id = item.text)) },
        selected = item == selectedItem.value,
        onClick = {
            selectedItem.value = item
            closeDrawer()
            if (item == DrawerElement.LOGOUT) {
                onSignOut(openScreen)
            } else {
                openScreen(item.destination)
            }
        },
        modifier = modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}