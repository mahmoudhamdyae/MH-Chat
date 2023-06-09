package com.mahmoudhamdyae.mhchat.ui.screens.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.AuthFormEvent
import com.mahmoudhamdyae.mhchat.ui.screens.auth.AuthFormState
import com.mahmoudhamdyae.mhchat.ui.screens.auth.ValidationEvent
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val useCase: BaseUseCase,
    logService: LogService
): ChatViewModel(logService) {

    var state by mutableStateOf(AuthFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: AuthFormEvent) {
        when (event) {
            is AuthFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is AuthFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is AuthFormEvent.Submit -> {
                submitData()
            }
            is AuthFormEvent.ForgotPassword -> {
                onForgotPasswordClick()
            }
            else -> {
                throw Exception("Unknown event")
            }
        }
    }

    private fun submitData() {
        val emailResult = useCase.validateEmail(state.email)
        val passwordResult = useCase.validatePassword(state.password, true)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun onSignInClick(navigate: (String) -> Unit) {
        launchCatching {
            useCase.logInUseCase(state.email, state.password)
            navigate(HomeDestination.route)
        }
    }

    private fun onForgotPasswordClick() {
        val emailResult = useCase.validateEmail(state.email)

        if (!emailResult.successful) {
            state = state.copy(emailError = emailResult.errorMessage)
            return
        }
        launchCatching {
            useCase.forgotPasswordUseCase(state.email)
            SnackBarManager.showMessage(R.string.recovery_email_sent)
        }
    }
}