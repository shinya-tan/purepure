package com.example.tabi_tabi

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MyLocationManager(
    private var context: Context?,
    mListner: OnLocationResultListener?
) : LocationCallback() {
  private var fusedLocationProviderClient: FusedLocationProviderClient? =
    LocationServices.getFusedLocationProviderClient(
        context!!
    )
  private var mListener: OnLocationResultListener? = mListner


  interface OnLocationResultListener {
    fun onLocationResult(locationResult: LocationResult?)
  }

  override fun onLocationResult(locationResult: LocationResult) {
    super.onLocationResult(locationResult)
    mListener!!.onLocationResult(locationResult)
  }

  fun startLocationUpdates() {
    if (ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
      Log.d(ContentValues.TAG, "Permission required")
      ActivityCompat.requestPermissions(
          (context as Activity?)!!, arrayOf(
              Manifest.permission.ACCESS_FINE_LOCATION
          ), LOCATION_REQUEST_CODE
      )
      return
    }

    // 端末の位置情報サービスが無効になっている場合、設定画面を表示して有効化を促す
    if (!isGPSEnabled) {
      showLocationSettingDialog()
      return
    }
    val request = LocationRequest()
    request.interval = 5000
    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    fusedLocationProviderClient!!.requestLocationUpdates(request, this, null)
  }

  fun stopLocationUpdates() {
    fusedLocationProviderClient!!.removeLocationUpdates(this)
  }

  private val isGPSEnabled: Boolean
    get() {
      val locationManager =
        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

  private fun showLocationSettingDialog() {
    AlertDialog.Builder(context)
      .setMessage("設定画面で位置情報サービスを有効にしてください")
      .setPositiveButton("設定") { dialog, which ->
      }
      .setNegativeButton(
          "キャンセル"
      ) { dialog, which ->
        //NOP
      }
      .create()
      .show()
  }

  companion object {
    private const val LOCATION_REQUEST_CODE = 1
  }
}