package com.example.tabi_tabi.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tabi_tabi.R
import com.example.tabi_tabi.fragment.PostFragment
import com.example.tabi_tabi.fragment.RecommendFragment
import com.example.tabi_tabi.fragment.TimeLineFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.unity3d.player.UnityPlayerActivity


class MainActivity : AppCompatActivity() {
  private val recommendFragment = RecommendFragment()
  private val timeLineFragment = TimeLineFragment()
  private val postFragment = PostFragment()
  private val SettingFragment = com.example.tabi_tabi.fragment.SettingFragment()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.container_fragment, timeLineFragment).addToBackStack(null).commit()
    val actionBar: ActionBar? = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)
    actionBar!!.show()
    footer.setOnNavigationItemReselectedListener { item ->
      val fragment = supportFragmentManager.beginTransaction()
      when (item.itemId) {
        R.id.navigation_recommend -> {
          fragment.replace(R.id.container_fragment, recommendFragment)
          supportActionBar!!.show()
        }

        R.id.navigation_timeline -> {
          fragment.replace(R.id.container_fragment, timeLineFragment)
          supportActionBar!!.show()
        }
        R.id.navigation_search -> {
          fragment.replace(R.id.container_fragment, postFragment)
          supportActionBar!!.show()
        }
        R.id.navigation_searchpost -> {
          val intent = Intent(applicationContext, MapsActivity::class.java)
          startActivity(intent)
        }
        R.id.navigation_AR -> {
          val intent = Intent(applicationContext, UnityPlayerActivity::class.java)
          startActivity(intent)
        }
      }
      fragment.addToBackStack(null).commit()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.heder_menu, menu);
    return true
  }


  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id: Int = item.itemId
    when (id) {
      android.R.id.home -> {
        supportFragmentManager.popBackStack()
      }
      R.id.setting -> {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.container_fragment, SettingFragment).commit()
      }
    }
    return super.onOptionsItemSelected(item)
  }
}

