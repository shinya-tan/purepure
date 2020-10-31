package com.example.tabi_tabi.fragment

import androidx.fragment.app.Fragment
import com.example.tabi_tabi.MyLocationManager

abstract class BaseFragment : Fragment(), MyLocationManager.OnLocationResultListener {
    private var locationManager: MyLocationManager? = null
    override fun onResume() {
        super.onResume()
        locationManager = MyLocationManager(context, this)
        locationManager!!.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        if (locationManager != null) {
            locationManager!!.stopLocationUpdates()
        }
    }
}