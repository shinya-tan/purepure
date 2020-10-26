package com.example.tabi_tabi .fragment

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore



class HomeFragment : Fragment() {
    //arrayListOf<String>
    var texts = arrayOf(
        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号"
    )
    var Input :String? = null

    private lateinit var database: DatabaseReference

    var arrayAdapter :ArrayAdapter<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val db = FirebaseFirestore.getInstance()
//        val docRef = db.collection("users").document("SLw7nCxHIaHp2Vt49ndj")
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                    texts += document.get("born").toString()
//                    Input = document.get("born").toString()
//                    Log.d(TAG, "Inputの中身 : ${Input}")
//                    texts += "これもテスト"
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }

        //ここからしたは入力
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }

        //俺が買いたやつこれも入力
//        val data = hashMapOf<String,Any>()
//        data["fish"] = "nanoka"
//        db.collection("group").add(data)
//            .addOnSuccessListener{
//                // 保存成功
//            }.addOnFailureListener{
//                // 保存失敗
//            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.actionBar?.title = "Home"
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document("SLw7nCxHIaHp2Vt49ndj")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    texts += document.get("born").toString()
                    Input = document.get("born").toString()
                    Log.d(TAG, "Inputの中身 : ${Input}")
                    texts += "これもテスト"
                     this.arrayAdapter = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_list_item_1).apply {
                        add("test")
                        add(Input)}
                    listview1.adapter = arrayAdapter
                    listview2.adapter = arrayAdapter
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


//        val docRef = db.collection("users").document("SLw7nCxHIaHp2Vt49ndj")
//        var Input :String ?= null

//        arrayAdapter.run {
//            remove("#台風14号");
//            //add(Input)
//        };


        image_sun.setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN)


    }


}
