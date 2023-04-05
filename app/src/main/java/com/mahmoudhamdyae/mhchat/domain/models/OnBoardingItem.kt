package com.mahmoudhamdyae.mhchat.domain.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mahmoudhamdyae.mhchat.R

class OnBoardingItem(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    companion object{
        fun getData(): List<OnBoardingItem>{
            return listOf(
                OnBoardingItem(R.drawable.default_image, R.string.on_boarding_title_1, R.string.on_boarding_text_1),
                OnBoardingItem(R.drawable.default_image, R.string.on_boarding_title_2, R.string.on_boarding_text_2),
                OnBoardingItem(R.drawable.default_image, R.string.on_boarding_title_3, R.string.on_boarding_text_3)
            )
        }
    }
}