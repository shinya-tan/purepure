package com.example.tabi_tabi.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.google.firebase.firestore.FirebaseFirestore
import com.unity3d.player.UnityPlayerActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    var texts = arrayOf(
        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号"
    )
    var input: String? = null

    var arrayAdapter: ArrayAdapter<String>? = null


    var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
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
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document("SLw7nCxHIaHp2Vt49ndj")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    texts += document.get("born").toString()
                    input = document.get("born").toString()
                    Log.d(TAG, "Inputの中身 : $input")
                    this.arrayAdapter = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_list_item_1
                    ).apply {
                        add("test")
                        add(input)
                    }
                    listview1.adapter = arrayAdapter
                    listview2.adapter = arrayAdapter
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        image_sun.setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN)
    }


}
