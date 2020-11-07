package com.example.tabi_tabi.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tabi_tabi.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var geo: GeoPoint? = null
    var db_result: QuerySnapshot? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        var db: FirebaseFirestore? = null

        db = FirebaseFirestore.getInstance()
        db!!.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                db_result = result
//                for (document in result) {
//                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
//                    geo = document.get("location") as GeoPoint?
//                    val sydney = LatLng(geo!!.getLatitude(), geo!!.getLongitude())
//                }
                mapFragment.getMapAsync(this)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
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
        for (document in db_result!!) {
            geo = document.get("location") as GeoPoint?
            Log.d(ContentValues.TAG, "geopointの中身 : ${geo}")
            mMap.addMarker(
                MarkerOptions().position(LatLng(geo!!.getLatitude(), geo!!.getLongitude())).title(
                    document.get("title").toString()
                )
            )
        }

//        mMap.addMarker(MarkerOptions().position(LatLng(36.2, 138.2)).title("Marker in Sydney"))
//        mMap.addMarker(MarkerOptions().position(LatLng(40.2, 138.2)).title("Marker in Sydney"))
        //val sydney = LatLng(geo!!.getLatitude(), geo!!.getLongitude())
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(36.2, 138.2)))
    }
}