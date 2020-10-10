package com.example.tabi_tabi

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val texts = arrayOf(
        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号", "jkl", "klm", "lmn", "mno", "nop"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, texts
        )
        val firstFragment = FirstFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_post, firstFragment)
        fragmentTransaction.commit()

        image_sun.setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN)
        footer.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.navigation_recommend -> {
                    val intent = Intent(this, ActivityReccomend::class.java)
                    startActivity(intent)
                }
            }
        }
        listview1.adapter = arrayAdapter
        listview2.adapter = arrayAdapter
    }

}
