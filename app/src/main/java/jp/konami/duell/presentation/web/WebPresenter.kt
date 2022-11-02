package jp.konami.duell.presentation.web

import jp.konami.duell.database.DatabaseModel
import jp.konami.duell.database.DatabaseStorage
import jp.konami.duell.navigation.Router
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope

@InjectViewState
class WebPresenter(
    private val storage: DatabaseStorage,
    private val router: Router
) : MvpPresenter<WebMvpView>() {

    fun onDomainLink() = router.navigateToStars()

    fun onLink(link: String) {
        presenterScope.launch {
            if ("sweetfall.live" in link) return@launch
            if (storage.dao().count() == 0) storage.dao()
                .insert(DatabaseModel(admin = false, user = link))
        }
    }
}