package com.mahmoudhamdyae.mhchat.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProfileViewModel @AssistedInject constructor(
    private val useCase: BaseUseCase,
    @Assisted("toUserId") userId: String,
    logService: LogService
): ChatViewModel(logService) {

    val chatId = useCase.getChatId(userId)

    fun updateProfileImage(imageUrl: Uri) {
        launchCatching {
            useCase.updateProfileUseCase(imageUri = imageUrl)
        }
    }

    fun updateUserName(userName: String) {
        launchCatching {
            useCase.updateProfileUseCase(userName = userName)
        }
    }

    fun updateBio(bio: String) {
        launchCatching {
            useCase.updateProfileUseCase(bio = bio)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("toUserId") toUserId: String,
        ): ProfileViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            toUserId: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(toUserId) as T
            }
        }
    }
}