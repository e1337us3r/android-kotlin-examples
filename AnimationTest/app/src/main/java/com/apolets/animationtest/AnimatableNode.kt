package com.apolets.animationtest

import android.animation.ObjectAnimator
import android.util.Log
import android.view.animation.LinearInterpolator
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import java.util.*
import kotlin.math.pow

class AnimatableNode : Node() {

    val random = Random(System.currentTimeMillis())

    private fun localPositionAnimator(dur: Long, vararg values: Any?): ObjectAnimator {
        return ObjectAnimator().apply {
            target = this@AnimatableNode
            propertyName = "localPosition"
            // Change animation duration. Gave some randomness to it.
            duration = dur
            interpolator = LinearInterpolator()

            setAutoCancel(true)
            // * = Spread operator, this will pass N `Any?` values instead of a single list `List<Any?>`
            setObjectValues(*values)
            // Always apply evaluator AFTER object values or it will be overwritten by a default one
            setEvaluator(VectorEvaluator())
        }
    }

    fun attack(earthPosition: Vector3) {

        val distanceFactor = calculateDistanceFactor(localPosition, earthPosition)

        // With min=2F and max=2.5F duration range is: 3.1s to 5s (1s of randomness)
        val duration = (1000 * distanceFactor).toLong() + random.nextLong() % 1000 + 1000
        Log.d("MYAPP", "factor: $distanceFactor, duration: $duration")

        val animation = localPositionAnimator(duration, localPosition, earthPosition)

        animation.start()
    }

    private fun calculateDistanceFactor(start: Vector3, end: Vector3): Double {
        return Math.sqrt((end.x - start.x).pow(2).toDouble() + (end.y - start.y).pow(2).toDouble() + (end.z - start.z).pow(2).toDouble())
    }


}