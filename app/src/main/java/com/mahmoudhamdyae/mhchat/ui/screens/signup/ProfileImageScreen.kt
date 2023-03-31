package com.mahmoudhamdyae.mhchat.ui.screens.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.basicButton
import com.mahmoudhamdyae.mhchat.common.ext.textButton
import com.mahmoudhamdyae.mhchat.common.ext.toImageUri
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.composable.ButtonWithIcon
import com.mahmoudhamdyae.mhchat.ui.composable.ProfileImage
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination

object ProfileImageDestination: NavigationDestination {
    override val route: String = "profile_image"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun ProfileImageScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    var hasImage by rememberSaveable { mutableStateOf(false) }
    var imageUri: Uri? by rememberSaveable { mutableStateOf(null) }
    var textButton: Int by rememberSaveable { mutableStateOf(R.string.skip_button) }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            hasImage = true
            imageUri = uri
            viewModel.saveProfileImage(imageUri!!)
            textButton = R.string.continue_button
        }
    }

    val context = LocalContext.current
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null)  {
                hasImage = true
                imageUri = bitmap.toImageUri(context)
                viewModel.saveProfileImage(imageUri!!)
                textButton = R.string.continue_button
            }
        }

    BasicToolBar(ProfileImageDestination.titleRes)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val imageModifier = Modifier
            .size(height = 300.dp, width = 300.dp)
            .padding(16.dp)
        if (hasImage) {
            ProfileImage(photoUri = imageUri, modifier = imageModifier)
        } else {
            ProfileImage(modifier = imageModifier)
        }

        ButtonWithIcon(
            text = R.string.camera_button,
            imageRes = R.drawable.photo_camera_24,
            modifier = Modifier.basicButton()
        ) {
            cameraLauncher.launch()
        }

        ButtonWithIcon(
            text = R.string.files_button,
            imageRes = R.drawable.file_open_24,
            modifier = Modifier.basicButton()
        ) {
            imagePicker.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        OutlinedButton(
            onClick = { openScreen(HomeDestination.route) },
            modifier = Modifier.textButton()
        ) {
            Text(text = stringResource(id = textButton))
        }
    }
}