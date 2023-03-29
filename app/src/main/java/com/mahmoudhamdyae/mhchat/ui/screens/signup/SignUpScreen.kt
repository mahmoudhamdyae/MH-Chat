package com.mahmoudhamdyae.mhchat.ui.screens.signup

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
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination

object SignUpDestination: NavigationDestination {
    override val route: String = "sign_up"
    override val titleRes: Int = R.string.app_name

}

@Composable
fun SignUpScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState
    val fieldModifier = Modifier.fieldModifier()
    val focusManager = LocalFocusManager.current

    BasicToolBar(SignUpDestination.titleRes)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.sign_up_screen_title),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
            modifier = Modifier.padding(start = 16.dp)
        )
        Row(modifier = Modifier.textButton(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.already_have_an_account))
            BasicTextButton(text = R.string.sign_in) {
                openAndPopUp(LogInDestination.route)
            }
        }
        UserNameField(uiState.userName, viewModel::onUserNameChange, focusManager, fieldModifier)
        EmailField(uiState.email, viewModel::onEmailChange, focusManager, fieldModifier)
        PasswordField(uiState.password, viewModel::onPasswordChange, focusManager, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, focusManager, fieldModifier)

        BasicButton(R.string.sign_up_button, Modifier.basicButton()) {
            viewModel.onSignUpClick(openAndPopUp)
        }
    }
}