package com.geekydroid.firebaselearn

import com.geekydroid.firebaselearn.data.PushNotification
import com.geekydroid.firebaselearn.utils.Constants.CONTENT_TYPE
import com.geekydroid.firebaselearn.utils.Constants.PROJECT_ID
import com.geekydroid.firebaselearn.utils.Constants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {


    @Headers(
        "Authorization: key=${SERVER_KEY}",
        "Content-Type:${CONTENT_TYPE}",
    )
    @POST("/fcm/send")
    suspend fun sendNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

}