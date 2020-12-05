package com.example.tabi_tabi.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_post.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PostFragment : BaseFragment() {

  private var altitude: Double? = null
  private var latitude: Double? = null
  private var longitude: Double? = null
  private var path: Uri? = null
  private lateinit var auth: FirebaseAuth
  lateinit var currentPhotoPath: String


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
    auth = Firebase.auth


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
      screen.visibility = View.VISIBLE
      uploadTask.addOnSuccessListener {
        val db = FirebaseFirestore.getInstance()
        val user = PostModel(
          titletext, posttext, location, altitude, createdAt,
          "images/${path!!.lastPathSegment}", auth.uid, 0
        )
        db.collection("posts")
          .document()
          .set(user)
          .addOnSuccessListener {
            screen.visibility = View.GONE
            Toast.makeText(context, "送信成功", Toast.LENGTH_SHORT).show();
          }
          .addOnFailureListener {
            screen.visibility = View.GONE
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
      1 -> {
        val values = ContentValues().apply {
          put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
          put("_data", currentPhotoPath)
        }
        activity!!.contentResolver.insert(
          MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val inputStream = FileInputStream(File(currentPhotoPath))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        imageView1.setImageBitmap(bitmap)
        path = Uri.fromFile(File(currentPhotoPath))
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


  override fun onResume() {
    super.onResume()

    button3.setOnClickListener {
      // カメラ機能を実装したアプリが存在するかチェック
      if (checkCameraPermission()) {
        takePicture()
      } else {
        grantCameraPermission()
      }
    }
  }

  //カメラの起動
  private fun takePicture() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      addCategory(Intent.CATEGORY_DEFAULT)
      putExtra(MediaStore.EXTRA_OUTPUT, createSaveFileUri())
    }
    startActivityForResult(intent, 1)
  }

  //カメラの許可を得ているか
  private fun checkCameraPermission() = PackageManager.PERMISSION_GRANTED ==
          ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)

  //カメラの許可が下りてなかったら許可とり
  private fun grantCameraPermission() =
    ActivityCompat.requestPermissions(
      (context as Activity?)!!,
      arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
      2
    )

  //撮った写真のURIの獲得
  private fun createSaveFileUri(): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
    val storageDir = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    if (!storageDir!!.exists()) {
      storageDir.mkdir()
    }
    val file = File.createTempFile(
      "JPEG_${timeStamp}_", /* prefix */
      ".jpg", /* suffix */
      storageDir      /* directory */
    ).apply {
      currentPhotoPath = absolutePath
    }
    return FileProvider.getUriForFile(context!!, "com.example.tabi_tabi", file)
  }

  //カメラの許可の最終確認
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    if (requestCode == 2) {
      if (grantResults.isNotEmpty() &&
        grantResults[0] == PackageManager.PERMISSION_GRANTED
      ) {
        takePicture()
      }
    }
  }
}

