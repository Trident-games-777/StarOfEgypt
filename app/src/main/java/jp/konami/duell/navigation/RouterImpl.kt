package jp.konami.duell.navigation

import android.app.Activity

class RouterImpl(
    private val callingActivity: Activity
) : Router {
    override fun navigateToStars() {
        callingActivity.startActivity(Intents.getStarsIntent(callingActivity))
        callingActivity.finish()
    }

    override fun navigateToWeb(url: String) {
        callingActivity.startActivity(Intents.getWebIntent(callingActivity, url))
        callingActivity.finish()
    }
}