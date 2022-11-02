package jp.konami.duell.presentation.starting

import moxy.MvpView
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SkipStrategy::class)
interface StartingMvpView : MvpView {
    fun startAdminService()
    fun loadingStarted()
}