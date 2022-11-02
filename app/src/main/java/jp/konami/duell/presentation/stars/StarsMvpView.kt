package jp.konami.duell.presentation.stars

import androidx.annotation.DrawableRes
import moxy.MvpView
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SkipStrategy::class)
interface StarsMvpView : MvpView {
    fun hideStars()
    fun setItem(@DrawableRes starItem: Int)
    fun startShining()
}