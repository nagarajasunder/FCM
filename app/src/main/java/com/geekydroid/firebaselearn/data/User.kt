package com.geekydroid.firebaselearn.data

import java.text.DateFormat

data class User(
    val userId: String = "",
    val emailAddress: String = "",
    val userToken: String = "",
    val createdOn: Long = System.currentTimeMillis()
) {


    val createdOnFormatted: String
        get() = DateFormat.getDateTimeInstance().format(createdOn)
}