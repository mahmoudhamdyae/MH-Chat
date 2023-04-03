package com.mahmoudhamdyae.mhchat.data.services

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.trace
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let {
                        User(userId = it.uid, email = it.email!!)
                    } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    /**
     * Log in
     */
    override suspend fun authenticate(email: String, password: String) {
        trace(AUTHENTICATE_TRACE) {
            auth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun sendRecoveryEmail(email: String) {
        trace(SEND_RECOVERY_EMAIL_TRACE) {
            auth.sendPasswordResetEmail(email).await()
        }
    }

    /**
     * Sign up a new user with email and password
     */
    override suspend fun linkAccount(email: String, password: String) {
        trace(LINK_ACCOUNT_TRACE) {
            auth.createUserWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun deleteAccount(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        trace(DELETE_ACCOUNT_TRACE) {
            auth.currentUser!!.reauthenticate(credential).await()
            auth.currentUser!!.delete().await()
        }
    }

    override suspend fun signOut() {
        trace(SIGN_OUT_TRACE) {
            auth.signOut()
        }
    }

    companion object {
        private const val AUTHENTICATE_TRACE = "linkAccount"
        private const val SEND_RECOVERY_EMAIL_TRACE = "linkAccount"
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
        private const val DELETE_ACCOUNT_TRACE = "linkAccount"
        private const val SIGN_OUT_TRACE = "linkAccount"

    }
}
