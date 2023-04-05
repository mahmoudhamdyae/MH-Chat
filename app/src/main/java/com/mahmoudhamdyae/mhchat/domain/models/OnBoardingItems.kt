package com.mahmoudhamdyae.mhchat.domain.models

import com.mahmoudhamdyae.mhchat.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.default_image, R.string.on_boarding_title_1, R.string.on_boarding_text_1),
                OnBoardingItems(R.drawable.default_image, R.string.on_boarding_title_2, R.string.on_boarding_text_2),
                OnBoardingItems(R.drawable.default_image, R.string.on_boarding_title_3, R.string.on_boarding_text_3)
            )
        }
    }
}