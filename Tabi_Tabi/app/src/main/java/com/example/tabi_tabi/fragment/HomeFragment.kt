package com.example.tabi_tabi .fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private val texts = arrayOf(
        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号", "jkl", "klm", "lmn", "mno", "nop"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.actionBar?.title = "Home"
        val arrayAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_list_item_1,
            texts
        )
        image_sun.setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN)
        listview1.adapter = arrayAdapter
        listview2.adapter = arrayAdapter
    }
}