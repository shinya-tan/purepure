package com.example.tabi_tabi.fragment

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.system.Os.remove
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class HomeFragment : BaseFragment() {
  var input: String? = null

  var arrayAdapter: ArrayAdapter<String>? = null
  var arrayList: ArrayList<String>? = ArrayList()

  private var latitude: Double? = null
  private var longitude: Double? = null

  var isFirst: Boolean = true
  private lateinit var auth: FirebaseAuth

  override fun onLocationResult(locationResult: LocationResult?) {
    if (locationResult == null) {
      Log.d(ContentValues.TAG, "# No location data.")
      return
    }
    auth = Firebase.auth
    // 緯度・経度を取得
    latitude = locationResult.lastLocation.latitude
    longitude = locationResult.lastLocation.longitude
    GlobalScope.launch {
      async {
        this@HomeFragment.getWeather(
          LatLng(
            latitude!!,
            longitude!!
          )
        )
      }.await()
    }
  }

  var db: FirebaseFirestore? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    onLocationResult(null)
    super.onCreate(savedInstanceState)

    this.db = FirebaseFirestore.getInstance()
    db!!.collection("posts")
      .get()
      .addOnSuccessListener { result ->
        for (document in result) {
          Log.d(TAG, "${document.id} => ${document.data}")
        }
      }
      .addOnFailureListener { exception ->
        Log.w(TAG, "Error getting documents.", exception)
      }

  }

  fun getWeather(latLng: LatLng) {
    if (isFirst) {
      val requestURL =
        "https://api.openweathermap.org/data/2.5/find?lat=${latLng.latitude}&lon=${latLng.longitude}&cnt=1&APPID=c9f50f3f23aab1a0695c0a7758b4f1e2"
      val url = URL(requestURL)
      val `is` = url.openConnection().getInputStream()
      val reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
      val sb = StringBuilder()
      var line: String?
      while (null != reader.readLine().also { line = it }) {
        sb.append(line)
      }
      var listwed: ArrayList<String> = ArrayList()
      var data = sb.split("[")
      var weddata = data[2].split(",")
      for (document in weddata) {
        listwed?.add(document.split(":").toString())
      }
      val handler = Handler(Looper.getMainLooper())
      handler.post {
        var weatherDescriptionList: MutableList<String> = listwed[2].split(",").toMutableList()
        weatherDescriptionList[1] = weatherDescriptionList[1].drop(2)
        weatherDescriptionList[1] = weatherDescriptionList[1].dropLast(2)
        if (weatherDescriptionList[1].equals("overcast clouds")) {
          println("どんよりとした雲")
        }
        var weatherIconList: MutableList<String> = listwed[3].split(",").toMutableList()
        weatherIconList[1] = weatherIconList[1].drop(2)
        weatherIconList[1] = weatherIconList[1].dropLast(7)
        Picasso.get()
          .load("https://openweathermap.org/img/w/" + weatherIconList[1] + ".png")
          .fit()
          .centerCrop()
          .into(image_wedther)
        isFirst = false
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    activity?.actionBar?.title = "Home"
    arrayList = ArrayList()
    val db = FirebaseFirestore.getInstance()
    db.collection("posts")
      .get()
      .addOnSuccessListener { documents ->
        for (document in documents) {
          val title = document.get("title").toString()
          arrayList!!.add(title)
        }
        this.arrayAdapter = ArrayAdapter<String>(
          context!!,
          android.R.layout.simple_list_item_1,
          arrayList!!
        )
        listview1.adapter = arrayAdapter
        listview2.adapter = arrayAdapter

      }
      .addOnFailureListener { exception ->
        Log.d(TAG, "get failed with ", exception)
      }
  }
}


