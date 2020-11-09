package com.example.tabi_tabi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.example.tabi_tabi.fragment.HomeFragment
import com.example.tabi_tabi.fragment.PostFragment
import com.example.tabi_tabi.fragment.RecommendFragment
import com.example.tabi_tabi.fragment.TimeLineFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.example.tabi_tabi.activity.MapsActivity

class MainActivity : AppCompatActivity() {
    val recommendFragment = RecommendFragment()
    val timeLineFragment = TimeLineFragment()
    val postFragment = PostFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        footer.setOnNavigationItemReselectedListener { item ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_recommend -> {
                    Log.d("test", "next")
                    fragmentTransaction.replace(R.id.container_fragment, recommendFragment)
                    supportActionBar!!.show()
                }

                R.id.navigation_timeline -> {
                    fragmentTransaction.replace(R.id.container_fragment, timeLineFragment)
                    supportActionBar!!.show()
                }
                R.id.navigation_search -> {
                    fragmentTransaction.replace(R.id.container_fragment, postFragment)
                    supportActionBar!!.show()
                }
                R.id.navigation_searchpost -> {
                    val intent = Intent(applicationContext, MapsActivity::class.java)
                    startActivity(intent)
                    supportActionBar!!.show()
                }
            }
            fragmentTransaction.addToBackStack(null).commit()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            supportFragmentManager.popBackStack()
            supportActionBar!!.hide()
        }
        return super.onOptionsItemSelected(item)
    }

}
