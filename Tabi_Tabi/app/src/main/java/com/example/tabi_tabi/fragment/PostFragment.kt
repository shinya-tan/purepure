package com.example.tabi_tabi.fragment

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_post.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PostFragment : BaseFragment() {

  private var altitude: Double? = null
  private var latitude: Double? = null
  private var longitude: Double? = null
  private var path: Uri? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_post, container, false)
  }

  override fun onLocationResult(locationResult: LocationResult?) {
    if (locationResult == null) {
      Log.d(ContentValues.TAG, "# No location data.")
      return
    }

    // 緯度・経度を取得
    latitude = locationResult.lastLocation.latitude
    longitude = locationResult.lastLocation.longitude
    altitude = locationResult.lastLocation.altitude

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    fun getNowDate(): String? {
      val df: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
      val date = Date(System.currentTimeMillis())
      return df.format(date)
    }

    button1.setOnClickListener {
      selectPhoto()
    }

    button2.setOnClickListener {
      val titletext = edit_text1!!.text.toString()
      val posttext = edit_text2!!.text.toString()
      val location = LatLng(latitude!!, longitude!!)
      val createdAt = getNowDate()
      val storageRef = Firebase.storage.reference
      val riversRef = storageRef.child("images/${path!!.lastPathSegment}")
      val uploadTask = riversRef.putFile(path!!)
      uploadTask.addOnSuccessListener {
        val db = FirebaseFirestore.getInstance()
        val user = PostModel(
          titletext, posttext, location, altitude, createdAt,
          "images/${path!!.lastPathSegment}", "usrID",0
        )
        db.collection("posts")
          .document()
          .set(user)
          .addOnSuccessListener {
            Toast.makeText(context, "送信成功", Toast.LENGTH_SHORT).show();
          }
          .addOnFailureListener {
            Toast.makeText(context, "送信失敗", Toast.LENGTH_SHORT).show();
          }
      }
        .addOnFailureListener { error ->
          Log.d("upload", "$error")
        }
    }

    activity?.actionBar?.title = "投稿"
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode != AppCompatActivity.RESULT_OK) {
      return
    }
    when (requestCode) {
      42 -> {
        try {
          data?.data?.also { uri ->
            val inputStream = activity!!.contentResolver.openInputStream(uri)
            val image = BitmapFactory.decodeStream(inputStream)
            imageView1.setImageBitmap(image)
            path = uri
          }
        } catch (e: Exception) {
          Toast.makeText(context, "エラーが発生しました", Toast.LENGTH_LONG).show()

        }
      }
    }
  }

  private fun selectPhoto() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "image/*"
    }
    startActivityForResult(intent, 42)
  }
}

