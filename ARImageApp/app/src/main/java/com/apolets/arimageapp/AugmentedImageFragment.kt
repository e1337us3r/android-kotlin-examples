package com.apolets.arimageapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class AugmentedImageFragment: ArFragment() {

    private val SAMPLE_IMAGE_DATABASE = "sample_database.imgdb"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config = super.getSessionConfiguration(session)
        setupAugmentedImageDatabase(config, session)
        config.focusMode = Config.FocusMode.AUTO
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session?) {
        val augmentedImageDb = AugmentedImageDatabase(session)
        val assetManager = context!!.assets
        val inputStream1 = assetManager.open("color.jpg")
        val augmentedImageBitmap1 = BitmapFactory.decodeStream(inputStream1)
        val inputStream2 = assetManager.open("social.jpg")
        val augmentedImageBitmap2 = BitmapFactory.decodeStream(inputStream2)
        augmentedImageDb.addImage("color", augmentedImageBitmap1)
        augmentedImageDb.addImage("social", augmentedImageBitmap2)
        config.augmentedImageDatabase = augmentedImageDb
    }



}