package com.example.madproject.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import com.example.madproject.R

class NotificationConfig (){

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private var channelId = "com.example.madproject.activities"
    private var description = "Test notification"

    //creating function to notify the user
     fun notifyHere( context : Context,title1:String, description1:String) {

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context,0, intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //configure the notification channel
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.CYAN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context,channelId)
                .setContentTitle(title1)
                .setContentText(description1)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)

        }else{
            //configure the notification channel
            builder = Notification.Builder(context)
                .setContentTitle(title1)
                .setContentText(description1)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)

        }
        notificationManager.notify(1234,builder.build())
    }

    companion object{
        //creating a static object to access the function
        val notifyObject : NotificationConfig = NotificationConfig()
    }
}