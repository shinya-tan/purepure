package com.example.tabi_tabi.fragment

//import com.example.tabi_tabi.model.TimeLineModel

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment : BaseFragment() {

    var altitude :Double? = null
    var latitude :Double? = null
    var longitude :Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
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

        button2!!.setOnClickListener {
            val titletext = edit_text1!!.text.toString()
            val posttext = edit_text2!!.text.toString()
            val location = LatLng(latitude!!,longitude!!)
            val db = FirebaseFirestore.getInstance()
            val post :PostModel = PostModel(
                titletext,posttext,location,altitude,"time","content","usrID")
            )
            //val user = TimeLineModel(1813, titletext, posttext)
//            db.collection("users")
//                .document()
//                .set(user)
//                .addOnSuccessListener {
//                    Toast.makeText(context, "送信成功", Toast.LENGTH_SHORT).show();
//                }
//                .addOnFailureListener {
//                    Toast.makeText(context, "送信失敗", Toast.LENGTH_SHORT).show();
//
//                }

        }

        activity?.actionBar?.title = "投稿"
    }


    }

