package com.example.tabi_tabi.activity

//import com.example.tabi_tabi.fragment.PostFragment
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tabi_tabi.R
import com.example.tabi_tabi.fragment.PostFragment
import com.example.tabi_tabi.fragment.RecommendFragment
import com.example.tabi_tabi.fragment.TimeLineFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        footer.setOnNavigationItemReselectedListener { item ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.navigation_recommend -> {
                    val recommendFragment = RecommendFragment()
                    fragmentTransaction.replace(R.id.container_fragment, recommendFragment)
                }

                R.id.navigation_timeline -> {
                    val timeLineFragment = TimeLineFragment()
                    fragmentTransaction.replace(R.id.container_fragment, timeLineFragment)
                }
                R.id.navigation_search -> {
                    val postFragment = PostFragment()
                    fragmentTransaction.replace(R.id.container_fragment, postFragment)
                }
            }
            fragmentTransaction.addToBackStack(null).commit()
        }
    }


    public fun setActionBarTitle(title: String?) {
        actionBar!!.title = title
    }
}
