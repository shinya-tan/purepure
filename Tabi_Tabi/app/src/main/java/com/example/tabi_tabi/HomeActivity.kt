package com.example.tabi_tabi

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val texts = arrayOf(
        "abc ", "bcd", "cde", "def", "efg",
        "fgh", "ghi", "hij", "ijk", "jkl", "klm", "lmn", "mno", "nop",
        "opq", "pqr", "qrs", "rst", "stu", "tuv", "uvw", "vwx", "wxy", "xyz"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_home)



        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, texts
        )

        listview1.setAdapter(arrayAdapter)

    }

}