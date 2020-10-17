package com.example.tabi_tabi.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.view.LayoutInflater
import com.example.tabi_tabi.R


class TimeLineContentsAdapter(
    context: Context,
    layoutid: Int,
    name: Array<String>,
    text: Array<String>
) : BaseAdapter() {
    private val mInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var context: Context? = context
    private var layoutId: Int = layoutid

    private val nameList: Array<String> = name
    private val emailList: Array<String> = text

    internal class ViewHolder {
        var text: TextView? = null
        var email: TextView? = null
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder

        var convertView = convertView

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_timeline_content, null)
            holder = ViewHolder()
            holder.text = convertView.findViewById(R.id.text_view)
            holder.email = convertView.findViewById(R.id.text_mail)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }


        val str = """
            Staff ID.170900${java.lang.String.valueOf(position + 1)}
            
            Email: ${emailList[position]}
            Tel: 020-8931-9933 #340${java.lang.String.valueOf(position + 1)}
            """.trimIndent()
        holder.email!!.setText(str)

        holder.text!!.setText(nameList[position])

        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return nameList.size
    }

}