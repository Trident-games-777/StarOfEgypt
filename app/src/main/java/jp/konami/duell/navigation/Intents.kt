package jp.konami.duell.navigation

import android.content.Context
import android.content.Intent
import jp.konami.duell.navigation.Extras.URL_EXTRA
import jp.konami.duell.presentation.stars.StarsActivity
import jp.konami.duell.presentation.web.WebActivity
import jp.konami.duell.services.AdminService

object Intents {
    fun getServiceIntent(callingContext: Context): Intent {
        return Intent(callingContext, AdminService::class.java)
    }

    fun getBroadcastIntent(admin: String): Intent {
        return Intent(Actions.ACTION_ADMIN_BROADCAST_RECEIVER).also {
            it.putExtra(Extras.ADMIN_EXTRA, admin)
        }
    }

    fun getStarsIntent(callingContext: Context): Intent {
        return Intent(callingContext, StarsActivity::class.java)
    }

    fun getWebIntent(callingContext: Context, url: String): Intent {
        return Intent(callingContext, WebActivity::class.java).also {
            it.putExtra(URL_EXTRA, url)
        }
    }
}