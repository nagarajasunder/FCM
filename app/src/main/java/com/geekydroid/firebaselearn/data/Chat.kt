package com.geekydroid.firebaselearn.data

data class Chat(
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timeStamp: Long = 0L
)