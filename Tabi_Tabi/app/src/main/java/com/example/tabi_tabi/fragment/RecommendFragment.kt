package com.example.tabi_tabi.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tabi_tabi.R
import com.example.tabi_tabi.activity.MapsActivity
import com.example.tabi_tabi.model.PostModel
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.android.synthetic.main.fragment_recommend.screen
import kotlinx.android.synthetic.main.fragment_time_line.*
import java.util.*
import kotlin.collections.ArrayList


class RecommendFragment : BaseFragment() {
  private val postModelList: ArrayList<PostModel>? = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_recommend, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    val db = FirebaseFirestore.getInstance()
    screen.visibility = View.VISIBLE
    db.collection("posts").get()
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          for (document in task.result!!) {
            val timeLineModel: PostModel =
              document.toObject(PostModel::class.java)
            postModelList!!.add(timeLineModel)
          }
          val shuffled: List<PostModel> = ArrayList(postModelList)
          Collections.shuffle(shuffled)
          val result: List<PostModel> = shuffled.subList(0, 3)
          button1.text = result[0].title
          button2.text = result[1].title
          button3.text = result[2].title
          val storage = Firebase.storage.reference
          result[0].content?.let {
            storage.child(it).downloadUrl.addOnSuccessListener { uri ->
              Log.d("image", uri.toString())
              Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .into(image_view1)
            }
          }
          result[1].content?.let {
            storage.child(it).downloadUrl.addOnSuccessListener { uri ->
              Log.d("image", uri.toString())
              Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .into(image_view2)
            }
          }
          button1.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("DB_LAT", result[0].location!!.latitude)
            intent.putExtra("DB_LNG", result[0].location!!.longitude)
            intent.putExtra("POSITION", postModelList!!.indexOf(result[0]))
            startActivity(intent)
          }
          button2.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("DB_LAT", result[1].location!!.latitude)
            intent.putExtra("DB_LNG", result[1].location!!.longitude)
            intent.putExtra("POSITION", postModelList!!.indexOf(result[1]))
            startActivity(intent)
          }
          button3.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("DB_LAT", result[2].location!!.latitude)
            intent.putExtra("DB_LNG", result[2].location!!.longitude)
            intent.putExtra("POSITION", postModelList!!.indexOf(result[2]))
            startActivity(intent)
          }
          screen.visibility = View.GONE
        } else {
          Log.d(
            "MissionActivity",
            "Error getting documents: ",
            task.exception
          )
        }
      }

    activity?.actionBar?.title = "お任せ"
  }

  override fun onLocationResult(locationResult: LocationResult?) {
    if (locationResult == null) {
      Log.d(TAG, "# No location data.")
      return
    }

  }
}


