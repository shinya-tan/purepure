package com.example.tabi_tabi

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.view.LayoutInflater


class TimeLineContentsAdapter : BaseAdapter {
    private val mInflater: LayoutInflater

    private var context: Context? = null
    private var layout_id:Int=0

    private val namelist: Array<String>
    private val emaillist: Array<String>

    internal class ViewHolder {
        var text: TextView? = null
        var email: TextView? = null
    }


    public constructor(context: Context, layoutid:Int, name:Array<String>,text:Array<String>) {
        namelist=name
        emaillist=text

        this.layout_id = layoutid
        this.context = context

        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder

        var convertView = convertView

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.timeline_content_layout, null)
            holder = ViewHolder()
            holder.text = convertView.findViewById(R.id.text_view)
            holder.email = convertView.findViewById(R.id.text_mail)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }


        val str = """
            Staff ID.170900${java.lang.String.valueOf(position + 1)}
            
            Email: ${emaillist[position]}
            Tel: 020-8931-9933 #340${java.lang.String.valueOf(position + 1)}
            """.trimIndent()
        holder.email!!.setText(str)

        holder.text!!.setText(namelist[position])

        return convertView!!
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        return namelist.size
    }

}