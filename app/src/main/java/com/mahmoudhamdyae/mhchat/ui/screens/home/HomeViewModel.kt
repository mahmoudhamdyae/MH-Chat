package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.data.services.PreferencesRepository
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
    preferencesRepository: PreferencesRepository,
    logService: LogService
): ChatViewModel(logService) {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()
    private val _lastMessages = MutableStateFlow<List<Message>>(emptyList())
    val lastMessages = _lastMessages.asStateFlow()

    private val isFirstTime: Flow<Boolean> =
        preferencesRepository.isFirstTime

    fun initialize(navigate: (String) -> Unit) {
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
            getChats()
        }
    }

    private fun getChats() {
        launchCatching {
            usersDatabaseService.userChats.collect { userChats ->
                userChats?.forEach { userChat ->
                    if (userChat != null) {
//                        _users.update { it + User("id1", "email1", userChat.toUserId) }
//                        _lastMessages.update { it + Message(userChat.chatId) }
                        // Get Users
                        usersDatabaseService.getUser(userChat.toUserId).collect { user ->
//                            if (user != null) _users.update { it + user }
                            _users.update { it + User("id1", "email1", "username1") }
                            // Get Last Messages
                            chatDatabaseService.lastMessage(userChat.chatId).collect { message ->
//                            if (message != null) _lastMessages.update { it + message }
                                _lastMessages.update { it + Message("body1") }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            navigate(LogInDestination.route)
        }
    }

    fun onItemClick(user: User, navigateTo: (String) -> Unit) {
        navigateTo("${MessagesDestination.route}/${user.userId}")
    }
}