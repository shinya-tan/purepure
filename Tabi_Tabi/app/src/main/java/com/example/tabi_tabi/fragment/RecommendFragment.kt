package com.example.tabi_tabi.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.tabi_tabi.R
import com.google.android.gms.location.*


class RecommendFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        activity?.actionBar?.title = "お任せ"
    }

    override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult == null) {
            Log.d(TAG,"# No location data.")
            return
        }

        // 緯度・経度を取得
        val latitude = locationResult.lastLocation.latitude
        Log.d(TAG, "${latitude}")
        val longitude = locationResult.lastLocation.longitude
    }
}


