package com.example.tabi_tabi.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_post.*
import java.util.*


data class User_Post(val title: String? = "", val post: String? = "")

class PostFragment : Fragment() {


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
//        editText1 = findViewById<View>(R.id.edit_text1) as EditText
//        editText2 = findViewById<View>(R.id.edit_text2) as EditText
//        button = findViewById<View>(R.id.button2) as Button
        button2!!.setOnClickListener { view ->
            val titletext = edit_text1!!.text.toString()
            val posttext = edit_text2!!.text.toString()
            val db = FirebaseFirestore.getInstance()
            val user = User_Post(titletext, posttext)
            db.collection("user_post")
                .document()
                .set(user)
                .addOnSuccessListener({
                    Toast.makeText(context, "送信成功", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener({
                    Toast.makeText(context, "送信失敗", Toast.LENGTH_SHORT).show();
                })

        }

        activity?.actionBar?.title = "投稿"
    }
}

//private val database: FirebaseFirestore get() = FirebaseFirestore.getInstance()
//private fun writeNewUser(tytle: String, post: String) {
//    val user = User_Post(tytle, post)
//    database.child("users").child(userId).setValue(user)
//}
//
//class TaskRepository {
//    private val database: FirebaseFirestore get() = FirebaseFirestore.getInstance()
//
//    suspend fun add(task: User_Post): Boolean {
//        try {
//            val collection = database.collection(COLLECTION_PATH)
//            val document = collection.document(task.tytle)
//            val data = task.toHashMap()
//            document.set(data).await()
//            return true
//        } catch (e: Exception) {
//            return false
//        }
//    }
//
//    companion object {
//        private const val COLLECTION_PATH = "user_post"
//    }
//}




