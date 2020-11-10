package com.example.tabi_tabi.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable


class PostModel : Serializable {
  var title: String? = null
  var description: String? = null
  var location: GeoPoint? = null
  var altitude: Double? = null
  var createdAt: String? = null
  var content: String? = null
  var userId: String? = null


  constructor()
  constructor(
    title: String?,
    descriptor: String?,
    location: LatLng,
    altitude: Double?,
    createdAt: String?,
    content: String?,
    userId: String?
  ) {
    this.title = title
    this.description = descriptor
    this.location = GeoPoint(location.latitude, location.longitude)
    this.altitude = altitude
    this.createdAt = createdAt
    this.content = content
    this.userId = userId
  }
}