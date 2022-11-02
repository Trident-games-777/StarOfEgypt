package jp.konami.duell.presentation.stars

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import jp.konami.duell.R
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class StarsActivity : MvpAppCompatActivity(), StarsMvpView {
    private lateinit var sky: GridLayout
    private lateinit var starButton: ImageView
    private lateinit var diamondButton: ImageView
    private lateinit var ufoButton: ImageView

    private val starsPresenter by moxyPresenter { StarsMvpPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stars)
        sky = findViewById(R.id.sky)
        starButton = findViewById(R.id.star)
        diamondButton = findViewById(R.id.diamond)
        ufoButton = findViewById(R.id.ufo)

        starButton.setOnClickListener { starsPresenter.onStarClicked() }
        diamondButton.setOnClickListener { starsPresenter.onDiamondClicked() }
        ufoButton.setOnClickListener { starsPresenter.onUfoClicked() }
    }

    override fun hideStars() = sky.children.forEach { it.alpha = 0f }

    override fun setItem(starItem: Int) {
        sky.children.forEach { (it as ImageView).setImageResource(starItem) }
    }

    override fun startShining() {
        val child = sky.children.toList().random()
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(child, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(child, View.ALPHA, 1f, 0f),
            )
            doOnEnd { startShining() }
            start()
        }
    }
}