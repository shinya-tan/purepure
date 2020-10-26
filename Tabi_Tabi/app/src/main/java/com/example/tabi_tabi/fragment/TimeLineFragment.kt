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
import com.example.tabi_tabi.model.TimeLineModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_time_line.*


class TimeLineFragment : Fragment() {
    private var db: FirebaseFirestore? = null
    private val timeLineList: ArrayList<TimeLineModel>? = ArrayList()

//    private val texts = arrayOf(
//        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
//        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号", "jkl", "klm", "lmn", "mno", "nop"
//    )

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

        db!!.collection("users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val timeLineModel: TimeLineModel =
                            document.toObject(TimeLineModel::class.java)
                        timeLineList?.add(timeLineModel)
                    }
                    val arrayAdapter: BaseAdapter = TimeLineContentsAdapter(
                        context!!, R.layout.item_timeline_content, timeLineList
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

