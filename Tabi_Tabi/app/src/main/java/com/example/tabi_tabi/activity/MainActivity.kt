package com.example.tabi_tabi.activity

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


class MainActivity : AppCompatActivity() {
    val recommendFragment = RecommendFragment()
    val timeLineFragment = TimeLineFragment()
    val postFragment = PostFragment()
    val fragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        //supportActionBar.hashCode()
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
            }
            fragmentTransaction.addToBackStack(null).commit()

        }
    }

    fun setActionBarTitle(title: String?) {
        actionBar!!.title = title
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
