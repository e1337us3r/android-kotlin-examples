package com.apolets.locationapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import com.apolets.locationapp.R.mipmap.ic_launcher
import android.support.v4.content.res.ResourcesCompat
import android.graphics.drawable.Drawable



class MainActivity : AppCompatActivity() {

    private lateinit var locationClient: FusedLocationProviderClient
    private var totalDistance: Double = 0.0
    private var lastLocation: Location? = null
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = applicationContext

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        setContentView(R.layout.activity_main)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        map.setTileSource(TileSourceFactory.MAPNIK)

        setupMap()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {

                    if (lastLocation != null) {
                        // Update total distance
                        val lastLat = (lastLocation as Location).latitude
                        val lastLon = (lastLocation as Location).longitude
                        totalDistance += distanceInMBetweenEarthCoordinates(lastLat, lastLon, location.latitude, location.longitude)
                    }

                    lastLocation = location

                    val toast = Toast.makeText(ctx, "Total Distance(m): ${totalDistance}", Toast.LENGTH_SHORT)
                    toast.show()


                    map.controller.setCenter(GeoPoint(location.latitude, location.longitude))

                }
            }
        }

        checkAndRequestPermission()

    }

    private fun setupMap() {
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        map.controller.setZoom(20.0)
        map.controller.setCenter(GeoPoint(60.17, 24.95))

        val myOverlay = MyLocationNewOverlay(map)

        val currentDraw = ResourcesCompat.getDrawable(resources, R.drawable.andy, null)
        var currentIcon: Bitmap? = null
        currentIcon = (currentDraw as BitmapDrawable).bitmap

        myOverlay.setPersonIcon(currentIcon)

        map.getOverlays().add(myOverlay)
    }

    private fun checkAndRequestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            // Permission has already been granted
            //getLocation()
            startLocationUpdates()
        }
    }


    fun degreesToRadians(degrees: Double): Double {
        return degrees * Math.PI / 180
    }

    fun distanceInMBetweenEarthCoordinates(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371

        val dLat = degreesToRadians(lat2 - lat1)
        val dLon = degreesToRadians(lon2 - lon1)

        val lat1R = degreesToRadians(lat1)
        val lat2R = degreesToRadians(lat2)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1R) * Math.cos(lat2R)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadiusKm * c * 1000
    }

    private fun startLocationUpdates() {
        locationClient.requestLocationUpdates(createLocationRequest(),
                locationCallback,
                Looper.getMainLooper())
    }

    fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        return locationRequest
    }

}
