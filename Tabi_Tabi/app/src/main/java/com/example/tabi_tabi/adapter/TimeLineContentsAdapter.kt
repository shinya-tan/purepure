package com.example.tabi_tabi.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.PostModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File


class TimeLineContentsAdapter(
    context: Context,
    layoutid: Int,
    name: ArrayList<PostModel>?
) : BaseAdapter() {
    private val mInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var layoutId: Int = layoutid
    private var nameList: ArrayList<PostModel>? = name
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null

    internal class ViewHolder {
        var text: TextView? = null
        var email: TextView? = null
        var image: ImageView? = null
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
        holder.email!!.text = nameList!![position].title

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