package com.mahmoudhamdyae.mhchat.ui.composable

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun messagesViewModelFactory(): MessagesViewModel.Factory
}

@Composable
fun messagesViewModel(
    chatId: String,
): MessagesViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).messagesViewModelFactory()

    return viewModel(
        factory = MessagesViewModel.provideFactory(factory, chatId)
    )
}