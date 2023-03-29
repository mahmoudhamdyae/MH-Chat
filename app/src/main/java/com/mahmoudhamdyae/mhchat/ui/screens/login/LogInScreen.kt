package com.mahmoudhamdyae.mhchat.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.basicButton
import com.mahmoudhamdyae.mhchat.common.ext.fieldModifier
import com.mahmoudhamdyae.mhchat.common.ext.textButton
import com.mahmoudhamdyae.mhchat.ui.composable.*
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.signup.SignUpDestination

object LogInDestination: NavigationDestination {
    override val route: String = "log_in"
    override val titleRes: Int = R.string.app_name

}

@Composable
fun LogInScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogInViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState
    val fieldModifier = Modifier.fieldModifier()
    val focusManager = LocalFocusManager.current

    BasicToolBar(title = LogInDestination.titleRes)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.log_in_screen_title),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
            modifier = Modifier.padding(start = 16.dp)
        )
        Row(modifier = Modifier.textButton(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.new_in_app))
            BasicTextButton(text = R.string.sig_up_for_free) {
                openAndPopUp(SignUpDestination.route)
            }
        }
        EmailField(uiState.email, viewModel::onEmailChange, focusManager, fieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, focusManager, fieldModifier)

        BasicTextButton(R.string.forgot_password, Modifier.textButton().align(Alignment.Start)) {
            viewModel.onForgotPasswordClick()
        }

        BasicButton(R.string.log_in_button, Modifier.basicButton()) {
            viewModel.onSignInClick(openAndPopUp)
        }

        GoogleButton(
            text = R.string.log_in_with_google_button,
            modifier = Modifier.basicButton()
        ) {
            viewModel.onSignInWithGoogleClick(openAndPopUp)
        }
    }
}