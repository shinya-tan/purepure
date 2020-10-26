package com.example.tabi_tabi.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.view.LayoutInflater
import com.example.tabi_tabi.R
import com.example.tabi_tabi.fragment.TimeLineModel


class TimeLineContentsAdapter(
    context: Context,
    layoutid: Int,
    name: ArrayList<TimeLineModel>?
) : BaseAdapter() {
    private val mInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var layoutId: Int = layoutid
    private var nameList: ArrayList<TimeLineModel>? = name

    internal class ViewHolder {
        var text: TextView? = null
        var email: TextView? = null
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder

        var convertView = convertView

        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, null)
            holder = ViewHolder()
            holder.text = convertView.findViewById(R.id.text_view)
            holder.email = convertView.findViewById(R.id.text_mail)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.email!!.text = nameList!![position].first

        holder.text!!.text = nameList!![position].last

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