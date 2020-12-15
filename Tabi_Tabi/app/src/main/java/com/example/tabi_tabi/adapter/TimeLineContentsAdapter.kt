package com.example.tabi_tabi.adapter

import android.content.ContentValues
import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.example.tabi_tabi.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting.*


class TimeLineContentsAdapter(
  private var context: Context,
  layoutid: Int,
  name: ArrayList<PostModel>?,
  private var documentIdList: ArrayList<String>?,
  user: ArrayList<UserModel>?
) : BaseAdapter() {
  private val mInflater: LayoutInflater =
    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
  private var layoutId: Int = layoutid
  private var nameList: ArrayList<PostModel>? = name
  private var userList: ArrayList<UserModel>? = user

  private var storage: FirebaseStorage? = null
  private var storageRef: StorageReference? = null
  var db: FirebaseFirestore? = null

  internal class ViewHolder {
    var userimage: ImageView? = null
    var username: TextView? = null
    var text: TextView? = null
    var email: TextView? = null
    var name: TextView? = null
    var image: ImageView? = null
    var like_button: ImageView? = null
    var like_number: TextView? = null
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val holder: ViewHolder

    var convertView = convertView
    storage = FirebaseStorage.getInstance()
    storageRef = storage!!.reference

    if (convertView == null) {
      convertView = mInflater.inflate(layoutId, null)
      holder = ViewHolder()
      holder.userimage = convertView.findViewById(R.id.user_image)
      holder.username = convertView.findViewById(R.id.text_username)
      holder.text = convertView.findViewById(R.id.text_view)
      holder.email = convertView.findViewById(R.id.text_mail)
      holder.image = convertView.findViewById(R.id.img_item)
      holder.like_button = convertView.findViewById(R.id.like_button)
      holder.like_number = convertView.findViewById(R.id.like_number)
      convertView.tag = holder
    } else {
      holder = convertView.tag as ViewHolder
    }
    holder.username!!.text = userList!![position].name
    if (Regex("https").containsMatchIn(userList!![position].icon!!)) {
      Picasso.get().load(userList!![position].icon).fit().centerCrop().into(holder.userimage)
    } else {
      userList!![position].icon?.let {
        storageRef!!.child(it).downloadUrl.addOnSuccessListener { uri ->
          Log.d("image", uri.toString())
          Picasso.get()
            .load(uri)
            .fit()
            .centerCrop()
            .into(holder.userimage)

        }
      }
    }
    nameList!![position].content?.let {
      storageRef!!.child(it).downloadUrl.addOnSuccessListener { uri ->
        Log.d("image", uri.toString())
        Picasso.get()
          .load(uri)
          .fit()
          .centerCrop()
          .into(holder.image)

      }
    }
    holder.text!!.text = nameList!![position].description
    holder.email!!.text = nameList!![position].title
    holder.like_number!!.text = nameList!![position].like.toString()


    holder.like_button!!.setOnClickListener {
      val toast = Toast.makeText(context, "いいねしました", Toast.LENGTH_LONG)
      toast.setGravity(Gravity.CENTER, 3, 3)
      toast.show()
      this.db = FirebaseFirestore.getInstance()
      val washingtonRef = db!!.collection("posts").document(documentIdList!![position])
      washingtonRef
        .update("like", nameList!![position].like?.plus(1))
      holder.like_number!!.text = nameList!![position].like?.plus(1).toString()
      nameList!![position].like?.toString()?.let { it1 -> Log.d(ContentValues.TAG, it1) }
      val mycolor: Int = context.getColor(android.R.color.holo_red_light)

      holder.like_button!!.setColorFilter(mycolor, PorterDuff.Mode.SRC_ATOP)
    }


    return convertView!!
  }

  override fun getItem(position: Int): Any {
    return position
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    if (this.nameList != null) {
      return this.nameList!!.size
    }
    return 0
  }

}