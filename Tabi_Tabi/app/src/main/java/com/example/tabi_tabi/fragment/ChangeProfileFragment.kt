package com.example.tabi_tabi.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.example.tabi_tabi.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_change_profile.*
import kotlinx.android.synthetic.main.fragment_change_profile.button1
import kotlinx.android.synthetic.main.fragment_change_profile.button2
import kotlinx.android.synthetic.main.fragment_change_profile.edit_text2
import kotlinx.android.synthetic.main.fragment_change_profile.imageView1
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ChangeProfileFragment : Fragment() {
  private var user: FirebaseUser? = null
  private var path: Uri? = null
  private lateinit var auth: FirebaseUser
  var db: FirebaseFirestore? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    user = Firebase.auth.currentUser
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_change_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val db = FirebaseFirestore.getInstance()
    this.auth = Firebase.auth.currentUser!!
    val refarence = db.collection("users")
    var user: UserModel? = null
    button1.setOnClickListener {
      selectPhoto()
    }
    button2.setOnClickListener {
      GlobalScope.launch {
        async {
          val storageRef = Firebase.storage.reference
          val riversRef = storageRef.child("images/${path!!.lastPathSegment}")
          val uploadTask = riversRef.putFile(path!!)
          var id: String? = null
          uploadTask.addOnSuccessListener {
            storageRef.child("images/${path!!.lastPathSegment}").downloadUrl.addOnSuccessListener {
              refarence
                .whereEqualTo("uid", auth.uid).get()
                .addOnSuccessListener { documents ->
                  for (document in documents) {
                    id = document.id
                    user = document.toObject(UserModel::class.java)
                    user!!.name = edit_text2.text.toString()
                    user!!.icon = "images/${path!!.lastPathSegment}"
                  }
                  FirebaseFirestore.getInstance().collection("users").document(id!!).delete()
                    .addOnSuccessListener {
                      FirebaseFirestore.getInstance().collection("users").document()
                        .set(user!!)
                        .addOnSuccessListener {
                          Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                          fragmentManager!!.popBackStack()
                        }
                        .addOnFailureListener {
                          Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
          }
            .addOnFailureListener { error ->
              Log.d("upload", "$error")
            }
        }.await()
      }

    }


    activity?.actionBar?.title = "プロフィール変更"
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