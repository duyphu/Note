package com.example.note.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import com.example.note.R;
import com.example.note.activity.MainActivity;
import com.example.note.config.Define;

/**
 * Created by phund on 3/3/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("note_title");
        int id = intent.getIntExtra("note_id", 0);
        Log.i("Time","chay vao receiver");
        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.putExtra("note_id", id);
        // post id of note
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.notes)
                .setContentTitle(Define.NOTIFICATION_TITLE)
                .setContentText(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTicker(Define.NOTIFICATION_TITLE)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
        notificationManager.notify(id,notification);
    }
}
