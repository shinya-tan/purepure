package com.example.tabi_tabi.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.google.android.gms.location.LocationResult
//import com.example.tabi_tabi.model.TimeLineModel
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.fragment_post.*
import java.util.*


class PostFragment : BaseFragment() {


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button2!!.setOnClickListener {
            val titletext = edit_text1!!.text.toString()
            val posttext = edit_text2!!.text.toString()
            val db = FirebaseFirestore.getInstance()
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

    override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult == null) {
            Log.d(ContentValues.TAG, "# No location data.")
            return
        }

        // 緯度・経度を取得
        val latitude = locationResult.lastLocation.latitude
        Log.d(ContentValues.TAG, "${latitude}")
        val longitude = locationResult.lastLocation.longitude
     }
    }

