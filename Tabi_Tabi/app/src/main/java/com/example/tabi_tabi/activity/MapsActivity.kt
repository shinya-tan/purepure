package com.example.tabi_tabi.activity

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tabi_tabi.MyLocationManager
import com.example.tabi_tabi.R
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
  MyLocationManager.OnLocationResultListener {

  private lateinit var mMap: GoogleMap

  var geo: GeoPoint? = null
  var dbResult: QuerySnapshot? = null
  var markerList: ArrayList<Marker> = ArrayList()
  private var locationManager: MyLocationManager? = null


  private var selectLat: Double? = null
  private var selectLng: Double? = null

  var position: Int? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    val actionBar: ActionBar? = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_maps)
    selectLat = intent.getDoubleExtra("DB_LAT", 0.0)
    selectLng = intent.getDoubleExtra("DB_LNG", 0.0)
    position = intent.getIntExtra("POSITION", -1)
    val mapFragment = supportFragmentManager
      .findFragmentById(R.id.map) as SupportMapFragment
    val db: FirebaseFirestore?

    db = FirebaseFirestore.getInstance()
    db.collection("posts")
      .get()
      .addOnSuccessListener { result ->
        dbResult = result
        val adapter = RecyclerAdapter(applicationContext, dbResult)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        adapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
          override fun onItemClickListener(
            view: View,
            position: Int,
            location: GeoPoint
          ) {
            mMap.moveCamera(
              CameraUpdateFactory.newLatLngZoom(
                LatLng(
                  location.latitude,
                  location.longitude
                ), 20.0f
              )
            )
            markerList[position].showInfoWindow()
          }
        })
        mapFragment.getMapAsync(this)
      }
      .addOnFailureListener { exception ->
        Log.w(ContentValues.TAG, "Error getting documents.", exception)
      }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id: Int = item.itemId
    if (id == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }


  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    mMap.uiSettings.isZoomControlsEnabled = true
    for (document in dbResult!!) {
      geo = document.get("location") as GeoPoint?
      val marker = mMap
        .addMarker(
          MarkerOptions()
            .position(LatLng(geo!!.latitude, geo!!.longitude))
            .title(
              document.get("title").toString()
            )
        )
      markerList.add(marker)
      mMap.setOnMarkerClickListener { marker ->
        val markerPosition = marker.position
        target_point.visibility = View.VISIBLE
        var selectedMarker = -1
        for (i in 0 until dbResult!!.size() - 1) {
          val location = dbResult!!.documents[i].get("location") as GeoPoint
          if (markerPosition?.latitude == location.latitude && markerPosition.longitude == location.longitude) {
            selectedMarker = i
          }
        }
        if (selectedMarker == -1) {
          return@setOnMarkerClickListener true
        }
        recyclerView.smoothScrollToPosition(selectedMarker)
        false
      }
    }
    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      return
    }

    if (position != -1) {
      recyclerView.smoothScrollToPosition(this.position!!)
      mMap.moveCamera(
        CameraUpdateFactory.newLatLngZoom(
          LatLng(
            selectLat!!,
            selectLng!!
          ), 20f
        )
      )
    } else {
      mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(26.1202, 127.7025)))

    }
    mMap.isMyLocationEnabled = true
    mMap.setOnMapClickListener {
      target_point.visibility = View.INVISIBLE
    }
  }


  override fun onResume() {
    super.onResume()
    locationManager = MyLocationManager(this, this)
    locationManager!!.startLocationUpdates()
  }

  override fun onLocationResult(locationResult: LocationResult?) {
  }

  override fun onPause() {
    super.onPause()
    if (locationManager != null) {
      locationManager!!.stopLocationUpdates()
    }
  }
}

class RecyclerAdapter(applicationContext: Context?, dbResult: QuerySnapshot?) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var mContext = applicationContext
  private var dataList = dbResult
  lateinit var listener: OnItemClickListener
  var iconList: ArrayList<Uri> = ArrayList()


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(mContext).inflate(R.layout.item_map_content, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val textTitle = holder.itemView.findViewById(R.id.text_title) as TextView
    val textDescription = holder.itemView.findViewById(R.id.text_description) as TextView
    val imageScene = holder.itemView.findViewById(R.id.img_item) as ImageView
    textTitle.text = dataList!!.documents[position].get("title").toString()
    textDescription.text = dataList!!.documents[position].get("description").toString()
    dataList!!.documents[position].get("content").let {
      storageRef.child(it as String).downloadUrl.addOnSuccessListener { uri ->
        Picasso.get()
          .load(uri)
          .fit()
          .centerCrop()
          .into(imageScene)
        iconList.add(uri)
      }
    }
    holder.itemView.setOnClickListener {
      listener.onItemClickListener(
        it,
        position,
        dataList!!.documents[position].get("location") as GeoPoint
      )
    }
  }

  interface OnItemClickListener {
    fun onItemClickListener(view: View, position: Int, location: GeoPoint)
  }

  fun setOnItemClickListener(listener: OnItemClickListener) {
    this.listener = listener
  }

  override fun getItemCount(): Int {
    return dataList!!.size()
  }

  private class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val mTextView: TextView = v.findViewById<View>(R.id.text_title) as TextView

  }
}

