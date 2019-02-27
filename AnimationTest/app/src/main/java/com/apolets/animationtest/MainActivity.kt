package com.apolets.animationtest

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var droidRenderable: ModelRenderable? = null

    val DEFAULT_MIN_SPAWN_DIST = 2F
    val DEFAULT_MAX_SPAWN_DIST = 2.5F

    val rGen = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment


        initResources()
        setPlaneTapListener()

    }

    private fun setPlaneTapListener() {

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            if (droidRenderable == null) {
                // Every renderable object must be initialized
                return@setOnTapArPlaneListener
            }


            // Center of our game, (0,0,0) point
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(arFragment.arSceneView.scene)


            AnimatableNode().also {
                it.setParent(anchorNode)
                it.renderable = droidRenderable
            }



            (1..30).forEach { _ ->
                AnimatableNode().also {
                    it.setParent(anchorNode)
                    it.renderable = droidRenderable
                    it.localPosition = randomCoord()
                    // Attack origin point, earth, runs animation
                    it.attack(Vector3(0f, 0f, 0f))
                }
            }


        }

    }

    private fun randomCoord(minDist: Float = DEFAULT_MIN_SPAWN_DIST, maxDist: Float = DEFAULT_MAX_SPAWN_DIST): Vector3 {

        var sign = if (rGen.nextBoolean()) 1 else -1

        //this formula distributes coordinates more evenly
        val x = (rGen.nextFloat() * (maxDist - minDist) + minDist) * sign

        sign = if (rGen.nextBoolean()) 1 else -1

        val z = (rGen.nextFloat() * (maxDist - minDist) + minDist) * sign

        val y = rGen.nextFloat() * maxDist

        //Log.d("MYAPP", "coordinates : x=$x, y=$y z=$z")

        return Vector3(x, y, z)
    }


    private fun initResources() {

        ModelRenderable.builder()
                .setSource(this, Uri.parse("andy.sfb"))
                .build()
                .thenAccept { droidRenderable = it }
    }
}
