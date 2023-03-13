package com.drax.sendit.view.login

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import app.siamak.sendit.R
import kotlinx.coroutines.flow.StateFlow

class RocketAnimationHandler(
    private val ivRocketAnimated: ImageView,
    lifecycle: Lifecycle,
    uiState: StateFlow<LoginUiState>,
) : LifecycleEventObserver {
    private var stopped = false
    private var succeed = false

    init {
        lifecycle.addObserver(this)
        lifecycle.coroutineScope.launchWhenStarted {
            uiState.collect {
                succeed = it is LoginUiState.LoginSucceed
            }
        }
    }

    private fun startLaunching() {
        with(R.drawable.rocket_launching.asVectorAnimated()) {
            ivRocketAnimated.setImageDrawable(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        if (!stopped) startLaunching()
                    }
                })
            }
            start()
        }
    }

    private fun startPreLaunch() {
        stopAndClear()
        with(R.drawable.rocket_pre_luaching.asVectorAnimated()) {
            ivRocketAnimated.alpha = 1f
            ivRocketAnimated.setImageDrawable(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        if (!stopped) {
                            startLaunching()
                            animateUp()
                        }
                    }
                })
            }
            start()
        }
    }


    fun startAnimation() {
        stopped = false
        with(R.drawable.login_rocket_animated.asVectorAnimated()) {
            ivRocketAnimated.setImageDrawable(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        if (!stopped) {
                            if (succeed) {
                                startPreLaunch()
                            } else {
                                startAnimation()
                            }
                        }
                    }
                })
            }
            start()
        }
    }

    private fun stopAndClear() {
        stopped = true
        (ivRocketAnimated.drawable as? AnimatedVectorDrawable)?.let {
            it.stop()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.clearAnimationCallbacks()
            }
        }
    }


    private fun animateUp() {
        ivRocketAnimated.animate()
            .setDuration(ANIMATION_UP_LENGTH_MILLIS)
            .translationY(-ANIMATION_UP_HEIGHT)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_STOP) {
            stopAndClear()
        } else if (event == Lifecycle.Event.ON_START) {
            startAnimation()
        }
    }

    private fun Int.asVectorAnimated() = AppCompatResources.getDrawable(
        ivRocketAnimated.context,
        this
    ) as AnimatedVectorDrawable

    companion object {
        private const val ANIMATION_UP_LENGTH_MILLIS = 2_000L
        private const val ANIMATION_UP_HEIGHT = 1_300F
    }
}
