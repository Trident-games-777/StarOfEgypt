package jp.konami.duell.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Settings
import jp.konami.duell.navigation.Intents

class AdminService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val admin = Settings.Global.getString(contentResolver, Settings.Global.ADB_ENABLED)
        //val admin = "0"
        sendBroadcast(Intents.getBroadcastIntent(admin))
        return START_STICKY
    }
}