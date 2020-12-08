package com.example.tabi_tabi.fragment

import android.content.ContentValues
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
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {
  private var user: FirebaseUser? = null
  private var path: Uri? = null
  private var userModel: UserModel? = null

  private lateinit var auth: FirebaseAuth
  var db: FirebaseFirestore? = null
  private val ChangeProfileFragment = com.example.tabi_tabi.fragment.ChangeProfileFragment()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_setting, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    user = Firebase.auth.currentUser
    db = FirebaseFirestore.getInstance()
    db!!.collection("users").whereEqualTo("uid", user!!.uid).get()
      .addOnSuccessListener { documents ->
        for (document in documents) {
          userModel = document.toObject(UserModel::class.java)
        }
        textView.text = userModel!!.name
        if (Regex("https").containsMatchIn(userModel!!.icon!!)) {
          Picasso.get().load(user!!.photoUrl).fit().centerCrop().into(imageView1)
        } else {
          val storage = FirebaseStorage.getInstance()
          val storageRef = storage.reference
          userModel!!.icon.let {
            storageRef.child(it!!).downloadUrl.addOnSuccessListener { uri ->
              Log.d("image", uri.toString())
              Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .into(imageView1)

            }
          }
        }
      }

    button.setOnClickListener {
      val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
      fragmentTransaction.replace(R.id.container_fragment, ChangeProfileFragment)
        .addToBackStack(null).commit()

    }
    activity?.actionBar?.title = "プロフィール"
  }

}