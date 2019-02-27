package com.apolets.arimageapp

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

val IMAGE_NAMES = arrayOf("", "")
const val TAG = "MYAPP"

class MainActivity : AppCompatActivity() {

    lateinit var fragment: ArFragment
    lateinit var renderables: HashMap<String, ModelRenderable>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.arimage_fragment) as ArFragment

        fragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            onUpdate(frameTime)
        }

        buildRenderables()

    }

    private fun buildRenderables() {

        Log.d(TAG,"Starting renderable building.")
        IMAGE_NAMES.forEach { imageName ->
            val renderable = ModelRenderable.builder()
                    .setSource(this, Uri.parse("$imageName.sfb"))
                    .build()
            renderable.thenAccept { it -> renderables[imageName] = it; Log.d(TAG,"$imageName renderable completed.") }
        }

    }


    private fun onUpdate(frameTime: FrameTime) {

        fragment.onUpdate(frameTime)
        val arFrame = fragment.arSceneView.arFrame
        if (arFrame == null || arFrame.camera.trackingState != TrackingState.TRACKING) {
            return
        }
        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        updatedAugmentedImages.forEach {
            when (it.trackingState) {
                TrackingState.PAUSED -> {
                    Log.d(TAG, "TrackingState.PAUSED")
                }

                TrackingState.STOPPED -> {
                    Log.d(TAG, "TrackingState.STOPPED")
                }
                TrackingState.TRACKING -> {
                    val anchors = it.anchors
                    if (anchors.isEmpty()) {
                        val pose = it.centerPose
                        val anchor = it.createAnchor(pose)
                        val anchorNode = AnchorNode(anchor)
                        anchorNode.setParent(fragment.arSceneView.scene)
                        val imgNode = TransformableNode(fragment.transformationSystem)
                        imgNode.setParent(anchorNode)

                        //find which image has been found and place it's renderable

                        for (imageName in IMAGE_NAMES) {
                            if (it.name == imageName) {
                                imgNode.renderable = renderables[imageName]
                                break
                            }
                        }
                    }
                }
                else -> { //tracking state is null}
                }
            }
        }


    }
}