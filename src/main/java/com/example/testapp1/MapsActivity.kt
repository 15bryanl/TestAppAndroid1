package com.example.testapp1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.annotation.ColorRes

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlin.math.PI
import kotlin.math.atan2

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var mymarker : Marker
    // For Polyline markers
    internal var listofPoints = ArrayList<LatLng>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Create button to move
        val button = findViewById<Button>(R.id.movebtn)
            movebtn.setOnClickListener{
                mymarker.rotation =
                    bearingTwoPoints(LatLng(40.730610,-73.935242), LatLng(39.730610,-75.935242)).toFloat()
                animateMarker(mymarker, LatLng(39.730610,-75.935242), Linear())
            }

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Google Maps Test"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val zoomlevel = 10.0f
        // Add a marker in Sydney and move the camera
        val newyork = LatLng(40.730610,-73.935242)

        // Add markers for polyline
            setLatlongs()
            addPolyLines()

        // Changed color of marker
        // mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
        // Changed image and size of marker
        mymarker = mMap.addMarker(MarkerOptions().position(newyork).title("mymarker").icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("portrait", 40, 60))))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newyork,zoomlevel))

    }

    /* Resize icon
    @param drawableName - string to take in name of drawable object
    @param width - int to change drawable size to
    @param height - int to change drawable size to
    @return - Bitmap resized correctly.
     */
    fun resizeBitmap(drawableName:String , width:Int , height:Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(resources,resources.getIdentifier(drawableName,"drawable", packageName))

        return Bitmap.createScaledBitmap(imageBitmap,width,height, false)
    }

    /* Set list of LatLongs for Polylines */
    fun setLatlongs(){
        listofPoints.add(LatLng(40.730610,-73.935242))
        //listofPoints.add(LatLng(41.730610,-72.935242))
        //listofPoints.add(LatLng(43.730610,-74.935242))
        //listofPoints.add(LatLng(42.730610,-76.935242))
        listofPoints.add(LatLng(39.730610,-75.935242))
    }
    /* Set add list of LatLongs for Polyline */
    fun addPolyLines(){
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(listofPoints)
        polylineOptions.width(5f).color(Color.YELLOW)
        mMap.addPolyline(polylineOptions)
    }

    /* interface */

    interface LatlnginterPolator{
        fun interpolate (fraction:Float , a: LatLng, b: LatLng):LatLng
    }

    class Linear : LatlnginterPolator{
        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
            val lat:Double = (b.latitude - a.latitude) * fraction + a.latitude
            val long:Double = (b.longitude - a.longitude) * fraction + a.longitude
            return LatLng(lat, long)
        }
    }

    /* Method for Animation */
    fun animateMarker(marker:Marker, finalposition:LatLng,latlnginterPolator: Linear){
        val startpos: LatLng = marker.position
        val handler = Handler()
        val start: Long = SystemClock.uptimeMillis()
        val interpolator: AccelerateDecelerateInterpolator= AccelerateDecelerateInterpolator()
        val durationms = 10000f
        handler.post(object :Runnable{
            var elapsed: Long = 0
            var t = 0f
            var v = 0f

            override fun run() {
                elapsed = SystemClock.uptimeMillis()-start
                t = elapsed/durationms
                v = interpolator.getInterpolation(t)
                val newpos: LatLng = latlnginterPolator.interpolate(v, startpos, finalposition)
                let{
                    marker.position = newpos
                }
                if(t<1){
                    handler.postDelayed(this, 16)
                }
            }
        }

        )
    }

    /* Method for Rotation */
    private fun degreeToRadians(degree:Double):Double{
        return degree * PI / 180.0
    }

    private fun radianToDegree(radian:Double):Double{
        return radian * 180.0/ PI
    }
    /* Calculate rotation */
    private fun bearingTwoPoints(latlng1:LatLng,latlng2:LatLng): Double {
        // Lat and Long for 1
        val lat1: Double = degreeToRadians(latlng1.latitude)
        val long1: Double = degreeToRadians(latlng1.longitude)
        // Lat and Long for 2
        val lat2: Double = degreeToRadians(latlng2.latitude)
        val long2: Double = degreeToRadians(latlng2.longitude)
        // Calculate degree
        val distancelong : Double = long2 - long1
        val y : Double = Math.sin(distancelong) * Math.cos(lat2)
        val X : Double = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2)*Math.cos(distancelong)
        val radianBearing : Double = atan2(y,X)
        return radianToDegree(radianBearing)
    }
}
