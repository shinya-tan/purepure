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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_time_line.*


class TimeLineFragment : Fragment() {
    private var db: FirebaseFirestore? = null
    val mMissionsList: ArrayList<String>? = null

    private val texts = arrayOf(
        "#クイズ ", "#首里城", "#無観客ライブ", "#剣持ボイス出せ", "#callioP",
        "#gawrt", "#chumbuds", "#FBKbirthday2020", "#台風14号", "jkl", "klm", "lmn", "mno", "nop"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").get()
            .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                override fun onComplete(task: Task<QuerySnapshot>) {
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val miss: String = document.toObject(String::class.java)
                            mMissionsList?.add(miss)
                        }
                    } else {
                        Log.d(
                            "MissionActivity",
                            "Error getting documents: ",
                            task.exception
                        )
                    }
                }
            })


        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arrayAdapter: BaseAdapter = TimeLineContentsAdapter(
            context!!, R.layout.item_timeline_content, mMissionsList, mMissionsList
        )

        timelineListView.adapter = arrayAdapter
        activity?.actionBar?.title = "タイムライン"


    }
}

class Timelinemodel {
    var born: String? = null
        private set
    var first: Number? = null
        private set
    var last: String? = null
        private set


    constructor() {}
    constructor(
        title: String?,
        exp: Number?,
        date: String?,
        description: String?
    ) {
        this.born = born
        this.first = first
        this.last = last
    }
}