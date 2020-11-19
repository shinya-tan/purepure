package com.example.tabi_tabi.adapter

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class TimeLineContentsAdapter(
    context: Context,
    layoutid: Int,
    name: ArrayList<PostModel>?,
    documentIdList: ArrayList<String>?
) : BaseAdapter() {
    private val mInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var layoutId: Int = layoutid
    private var nameList: ArrayList<PostModel>? = name
    private var documentIdList: ArrayList<String>? = documentIdList
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    var db: FirebaseFirestore? = null

    internal class ViewHolder {
        var text: TextView? = null
        var email: TextView? = null
        var image: ImageView? = null
        var like_button: ImageView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder

        var convertView = convertView
        storage = FirebaseStorage.getInstance()
        storageRef = storage!!.reference

        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, null)
            holder = ViewHolder()
            holder.text = convertView.findViewById(R.id.text_view)
            holder.email = convertView.findViewById(R.id.text_mail)
            holder.image = convertView.findViewById(R.id.img_item)
            holder.like_button = convertView.findViewById(R.id.like_button)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
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

        holder.like_button!!.setOnClickListener{
            Log.d(ContentValues.TAG, "nameList.likeの中身 :")
            Log.d(ContentValues.TAG, nameList!![position].like.toString())
            this.db = FirebaseFirestore.getInstance()
            val washingtonRef = db!!.collection("posts").document(documentIdList!![position])
            washingtonRef
                .update("like", nameList!![position].like?.plus(1))

            Log.d(ContentValues.TAG, "nameList.likeの中身 :")
            Log.d(ContentValues.TAG, nameList!![position].like.toString())
        }

        holder.text!!.text = nameList!![position].description

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