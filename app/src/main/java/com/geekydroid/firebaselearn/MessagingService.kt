package com.geekydroid.firebaselearn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geekydroid.firebaselearn.ui.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


private const val CHANNEL_ID = "MY_NOTIFICATION_CHANNEL"

private const val TAG = "MessagingService"

class MessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: called $token")
        updateToken(token)

    }

    private fun updateToken(token: String) {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            updateTokenViaSharedPrefs(token)
        } else {
            updateTokenToServer(currentUser.uid, token)
        }


    }

    private fun updateTokenToServer(uid: String, token: String) {
        val database =
            FirebaseDatabase.getInstance().reference.child("users").child(uid).child("token")
        database.setValue(token)
    }

    private fun updateTokenViaSharedPrefs(value: String) {
        val prefs: SharedPreferences? = getSharedPreferences("token", MODE_PRIVATE)
        prefs?.edit()?.putString("token", value)?.apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {

        Log.d(TAG, "onMessageReceived: called ${message.data}")

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT or FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.user)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "MY_CHANNEL"

        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "MY notifcation channel"

        }

        notificationManager.createNotificationChannel(channel)
    }


}
















