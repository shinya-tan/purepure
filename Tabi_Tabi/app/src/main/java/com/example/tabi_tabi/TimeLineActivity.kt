package com.example.tabi_tabi

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.footer
import kotlinx.android.synthetic.main.activity_time_line.*

class TimeLineActivity : AppCompatActivity() {


    private val texts = arrayOf(
        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号", "jkl", "klm", "lmn", "mno", "nop"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)


        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        val arrayAdapter:BaseAdapter = TimeLineContentsAdapter(
            this,R.layout.timeline_content_layout, texts, texts
        )

        timelineListView.adapter = arrayAdapter
    }
}