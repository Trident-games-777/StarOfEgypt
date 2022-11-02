package jp.konami.duell.presentation.starting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import jp.konami.duell.R
import jp.konami.duell.database.DatabaseStorage
import jp.konami.duell.navigation.Actions.ACTION_ADMIN_BROADCAST_RECEIVER
import jp.konami.duell.navigation.Extras.ADMIN_EXTRA
import jp.konami.duell.navigation.Intents
import jp.konami.duell.navigation.RouterImpl
import jp.konami.duell.utils.SecurityState
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class StartingActivity : MvpAppCompatActivity(), StartingMvpView {
    lateinit var receiver: BroadcastReceiver

    private var keepSplash = true
    private val startingPresenter by moxyPresenter {
        StartingPresenter(DatabaseStorage(applicationContext), RouterImpl(this))
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(ACTION_ADMIN_BROADCAST_RECEIVER))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplash }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val securityState =
                    SecurityState.from(intent?.getStringExtra(ADMIN_EXTRA).toString())
                if (securityState == SecurityState.USER) keepSplash = false
                startingPresenter.onSecurityStateReturned(securityState, this@StartingActivity)
                stopService(Intents.getServiceIntent(this@StartingActivity))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    override fun startAdminService() {
        startService(Intents.getServiceIntent(this))
    }

    override fun loadingStarted() {
        keepSplash = false
    }
}