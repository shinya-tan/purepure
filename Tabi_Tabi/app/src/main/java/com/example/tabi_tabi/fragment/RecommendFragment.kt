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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.android.synthetic.main.fragment_recommend.screen
import kotlinx.android.synthetic.main.fragment_time_line.*
import kotlin.collections.ArrayList


class RecommendFragment : BaseFragment() {
  private val postModelListOrderByLike: ArrayList<PostModel>? = ArrayList()
  private val postModelListTitle: ArrayList<String?>? = ArrayList()

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
    val database = FirebaseFirestore.getInstance()
    screen.visibility = View.VISIBLE
    database.collection("posts").orderBy("like")
      .get()
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          for (document in task.result!!) {
            val timeLineModel: PostModel =
              document.toObject(PostModel::class.java)
            postModelListOrderByLike!!.add(timeLineModel)
          }
          val shuffled: List<PostModel> = ArrayList(postModelListOrderByLike)
          val result: List<PostModel> = shuffled
          val MaxListNumber: Int = result.count()

          database.collection("posts")
            .get()
            .addOnCompleteListener { task ->
              if (task.isSuccessful) {
                for (document in task.result!!) {
                  val timeLineModel: PostModel =
                    document.toObject(PostModel::class.java)
                  postModelListTitle!!.add(timeLineModel.title)
                }
              }
            }
          button1.text = result[MaxListNumber - 1].title
          button2.text = result[MaxListNumber - 2].title
          button3.text = result[0].title
          val storage = Firebase.storage.reference
          result[MaxListNumber - 1].content?.let {
            storage.child(it).downloadUrl.addOnSuccessListener { uri ->
              Log.d("image", uri.toString())
              Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .into(image_view1)
            }
          }
          result[MaxListNumber - 2].content?.let {
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
            intent.putExtra("DB_LAT", result[MaxListNumber - 1].location!!.latitude)
            intent.putExtra("DB_LNG", result[MaxListNumber - 1].location!!.longitude)
            intent.putExtra(
              "POSITION",
              postModelListTitle!!.indexOf(result[MaxListNumber - 1].title)
            )
            startActivity(intent)
          }
          button2.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("DB_LAT", result[MaxListNumber - 2].location!!.latitude)
            intent.putExtra("DB_LNG", result[MaxListNumber - 2].location!!.longitude)
            intent.putExtra(
              "POSITION",
              postModelListTitle!!.indexOf(result[MaxListNumber - 2].title)
            )
            startActivity(intent)
          }
          button3.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra("DB_LAT", result[0].location!!.latitude)
            intent.putExtra("DB_LNG", result[0].location!!.longitude)
            intent.putExtra("POSITION", postModelListTitle!!.indexOf(result[0].title))
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


