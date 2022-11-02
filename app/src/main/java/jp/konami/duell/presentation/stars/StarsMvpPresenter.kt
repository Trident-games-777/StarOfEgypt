package jp.konami.duell.presentation.stars

import jp.konami.duell.R
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class StarsMvpPresenter : MvpPresenter<StarsMvpView>() {

    override fun onFirstViewAttach() {
        viewState.hideStars()
        viewState.startShining()
    }

    fun onStarClicked() = viewState.setItem(R.drawable.star)

    fun onDiamondClicked() = viewState.setItem(R.drawable.diamond)

    fun onUfoClicked() = viewState.setItem(R.drawable.ufo)

}