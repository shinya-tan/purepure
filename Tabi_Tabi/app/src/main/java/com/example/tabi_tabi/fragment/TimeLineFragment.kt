package com.example.tabi_tabi.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.example.tabi_tabi.activity.MapsActivity
import com.example.tabi_tabi.adapter.TimeLineContentsAdapter
import com.example.tabi_tabi.model.PostModel
import com.example.tabi_tabi.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_time_line.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import android.view.View as View1


class TimeLineFragment : Fragment(), AdapterView.OnItemClickListener {
  private var db: FirebaseFirestore? = null
  private var postModelList: ArrayList<PostModel>? = ArrayList()
  private var documentIdList: ArrayList<String> = ArrayList()
  private val userModelList: ArrayList<UserModel> = ArrayList()


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View1? {
    return inflater.inflate(R.layout.fragment_time_line, container, false)
  }

  override fun onViewCreated(view: View1, savedInstanceState: Bundle?) {
    this.db = FirebaseFirestore.getInstance()
    screen.visibility = View1.VISIBLE

    activity?.actionBar?.title = "タイムライン"
    postModelList = null
    postModelList = ArrayList()
    val handler = Handler(Looper.getMainLooper())
    GlobalScope.launch() {
      dbGet()
      handler.post {
        val arrayAdapter: BaseAdapter = TimeLineContentsAdapter(
          context!!, R.layout.item_timeline_content, postModelList, documentIdList, userModelList
        )
        timelineListView.adapter = arrayAdapter
        timelineListView.onItemClickListener = this@TimeLineFragment
        screen.visibility = View1.GONE
      }

    }
  }

  override fun onItemClick(p0: AdapterView<*>?, p1: View1?, p2: Int, p3: Long) {
    val intent = Intent(context, MapsActivity::class.java)
    intent.putExtra("DB_LAT", postModelList!![p2].location!!.latitude)
    intent.putExtra("DB_LNG", postModelList!![p2].location!!.longitude)
    intent.putExtra("POSITION", p2)
    startActivity(intent)
  }

  private suspend fun dbGet() = runBlocking {
    val querySnapshot = db!!.collection("posts").get().await()
    for (document in querySnapshot) {
      documentIdList.add(document.id)
      val timeLineModel: PostModel =
        document.toObject(PostModel::class.java)
      postModelList?.add(timeLineModel)
      Log.d("test", "1")

    }
    Log.d("test", "2")

    for (postmodel in postModelList!!) {
      val documents = db!!.collection("users")
        .whereEqualTo("uid", postmodel.userId)
        .get().await()
      for (document in documents) {
        userModelList.add(document.toObject(UserModel::class.java))
        Log.d("test", "check")
      }
    }
  }
}
