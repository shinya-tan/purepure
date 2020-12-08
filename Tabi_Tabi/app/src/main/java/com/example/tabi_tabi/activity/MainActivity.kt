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
import com.example.tabi_tabi.fragment.SettingFragment
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

    val actionBar: ActionBar? = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)
    actionBar!!.show()
    footer.setOnNavigationItemReselectedListener { item ->
      val fragmentTransaction = supportFragmentManager.beginTransaction()
      when (item.itemId) {
        R.id.navigation_recommend -> {
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
        }
        R.id.navigation_AR -> {
          val intent = Intent(applicationContext, UnityPlayerActivity::class.java)
          startActivity(intent)
        }
      }
      fragmentTransaction.addToBackStack(null).commit()

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
        fragmentTransaction.replace(R.id.container_fragment, SettingFragment)
        fragmentTransaction.addToBackStack(null).commit()
        supportActionBar!!.show()
      }
    }
    return super.onOptionsItemSelected(item)
  }
}

