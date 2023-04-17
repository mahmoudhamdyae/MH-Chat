package com.mahmoudhamdyae.mhchat.domain.usecases

data class BaseUseCase (
    val deleteAccountUseCase: DeleteAccountUseCase,
    val forgotPasswordUseCase: ForgotPasswordUseCase,
    val getChatId: GetChatId,
    val getChatsUseCase: GetChatsUseCase,
    val getMessagesUseCase: GetMessagesUseCase,
    val getUsersUseCase: GetUsersUseCase,
    val getUserUseCase: GetUserUseCase,
    val logInUseCase: LogInUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val signOutUseCase: SignOutUseCase,
    val signUpUseCase: SignUpUseCase,
    val updateProfileUseCase: UpdateProfileUseCase,
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword,
    val validateRepeatedPassword: ValidateRepeatedPassword,
    val validateUserName: ValidateUserName,
)