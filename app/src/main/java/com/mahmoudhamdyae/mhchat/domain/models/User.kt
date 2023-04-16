package com.mahmoudhamdyae.mhchat.domain.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

data class UserChat(
    val toUserId: String = "",
    val chatId: String = "",
)

@Parcelize
data class User(
    val userId: String = "",
    val email: String = "",
    val userName: String = "",
    val imageUrl: String? = null,
    val token: String = "",
): Parcelable

class AssetParamType: NavType<User>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): User? {
        @Suppress("DEPRECATION")
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: User) {
        bundle.putParcelable(key, value)
    }
}

fun User.toJson(): String {
    return Uri.encode(Gson().toJson(this))
}
