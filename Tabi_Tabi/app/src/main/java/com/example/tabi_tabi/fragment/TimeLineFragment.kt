package com.example.tabi_tabi.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.example.tabi_tabi.adapter.TimeLineContentsAdapter
import com.example.tabi_tabi.model.PostModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_time_line.*


class TimeLineFragment : Fragment() {
    private var db: FirebaseFirestore? = null
    private val postModelList: ArrayList<PostModel>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.db = FirebaseFirestore.getInstance()
        activity?.actionBar?.title = "タイムライン"
        db!!.collection("posts").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val timeLineModel: PostModel =
                            document.toObject(PostModel::class.java)
                        postModelList?.add(timeLineModel)
                    }
                    val arrayAdapter: BaseAdapter = TimeLineContentsAdapter(
                        context!!, R.layout.item_timeline_content, postModelList
                    )
                    timelineListView.adapter = arrayAdapter
                } else {
                    Log.d(
                        "MissionActivity",
                        "Error getting documents: ",
                        task.exception
                    )
                }
            }
    }
}
