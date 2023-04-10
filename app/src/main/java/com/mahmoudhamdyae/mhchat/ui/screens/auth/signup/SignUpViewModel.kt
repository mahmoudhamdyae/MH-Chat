package com.mahmoudhamdyae.mhchat.ui.screens.auth.signup

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidateEmail
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidatePassword
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidateRepeatedPassword
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidateUserName
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.AuthFormEvent
import com.mahmoudhamdyae.mhchat.ui.screens.auth.AuthFormState
import com.mahmoudhamdyae.mhchat.ui.screens.auth.ValidationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validateUserName: ValidateUserName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val accountService: AccountService,
    private val databaseService: UsersDatabaseService,
    private val storageService: StorageService,
    logService: LogService
): ChatViewModel(logService) {

    var state by mutableStateOf(AuthFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: AuthFormEvent) {
        when (event) {
            is AuthFormEvent.UserNameChanged -> {
                state = state.copy(userName = event.userName)
            }
            is AuthFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is AuthFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is AuthFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is AuthFormEvent.Submit -> {
                submitData()
            }
            else -> {
                throw Exception("Unknown event")
            }
        }
    }

    private fun submitData() {
        val userNameResult = validateUserName(state.userName)
        val emailResult = validateEmail(state.email)
        val passwordResult = validatePassword(state.password)
        val repeatedPasswordResult = validateRepeatedPassword(
            state.password, state.repeatedPassword
        )

        val hasError = listOf(
            userNameResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                userNameError = userNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun onSignUpClick(navigate: (String) -> Unit) {
        launchCatching {
            accountService.linkAccount(state.email, state.password)
            databaseService.saveUser(
                User(
                    userId = accountService.currentUserId,
                    email = state.email,
                    userName = state.userName
                )
            )
            navigate(ProfileImageDestination.route)
        }
    }

    fun saveProfileImage(imageUri: Uri) {
        launchCatching {
            storageService.saveImage(imageUri)
            databaseService.
                updateProfileImage(storageService.getImage(accountService.currentUserId).toString())
        }
    }
}