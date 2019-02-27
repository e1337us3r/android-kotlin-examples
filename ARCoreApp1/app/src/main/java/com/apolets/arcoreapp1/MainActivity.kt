package com.apolets.arcoreapp1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    lateinit var fragment: ArFragment
    var testRenderable: Renderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        val renderableFuture = ViewRenderable.builder()
                .setView(this,R.layout.rendtext)
                .build()

        renderableFuture.thenAccept { it -> testRenderable = it; setScreenTapListener(); }



    }

    fun setTextTapListener(node: TransformableNode){

        node.setOnTapListener { _, _ ->
            Toast.makeText(this,"Hit!",Toast.LENGTH_SHORT).show()
        }
    }

    fun setScreenTapListener(){

        fragment.setOnTapArPlaneListener(
                BaseArFragment.OnTapArPlaneListener { hitResult, _, _ ->
                    if (testRenderable == null) return@OnTapArPlaneListener

                    val anchor = hitResult!!.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(fragment.arSceneView.scene)

                    val viewNode = TransformableNode(fragment.transformationSystem)

                    viewNode.setParent(anchorNode)
                    viewNode.renderable = testRenderable
                    viewNode.select()

                    setTextTapListener(viewNode)


                }
        )


    }

}
