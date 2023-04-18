package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.google.firebase.messaging.FirebaseMessaging
import com.mahmoudhamdyae.mhchat.data.services.PreferencesRepository
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.models.toJson
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fcm: FirebaseMessaging,
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService,
    private val useCase: BaseUseCase,
    preferencesRepository: PreferencesRepository,
    logService: LogService
): ChatViewModel(logService) {

    private var _uiState = MutableStateFlow<List<Pair<User?, Message?>>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val isFirstTime: Flow<Boolean> =
        preferencesRepository.isFirstTime

    fun initialize(navigate: (String) -> Unit, setCurrentUser: (User?) -> Unit) {
        if (!accountService.hasUser) {
            launchCatching {
                isFirstTime.collect {
                    if (it) {
                        navigate(OnBoardingDestination.route)
                        this.cancel()
                    } else {
                        navigate(LogInDestination.route)
                        this.cancel()
                    }
                }
            }
        } else {
            getCurrentUser(setCurrentUser)
            getChats()
            fcm.isAutoInitEnabled = true
        }
    }

    private fun getCurrentUser(setCurrentUser: (User?) -> Unit) {
        launchCatching {
            useCase.getUserUseCase().collect {
                setCurrentUser(it)
            }
        }
    }

    private fun getChats() {
        launchCatching {
            useCase.getChatsUseCase {
                _uiState.value = it
            }
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            useCase.signOutUseCase()
            navigate(LogInDestination.route)
            launchCatching {
                useCase.updateProfileUseCase(token = "")
            }
            fcm.isAutoInitEnabled = false
        }
    }

    fun onItemClick(user: User, navigateTo: (String) -> Unit) {
        launchCatching {
            usersDatabaseService.userChats.collect { userChats ->
                val chatId = userChats?.first {
                    it?.toUserId == user.userId
                }?.chatId
                navigateTo("${MessagesDestination.route}/${chatId}/${user.toJson()}")
            }
        }
    }
}