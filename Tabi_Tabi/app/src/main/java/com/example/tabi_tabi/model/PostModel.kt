package com.example.tabi_tabi.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint


class PostModel {
    var title: String? = null
    var description: String? = null
    var location: GeoPoint? = null
    var altitude: Number? = null
    var createdAt: Timestamp? = null
    var content: String? = null
    var userId: String? = null


    constructor()
    constructor(
        title: String?,
        descriptor: String?,
        location: GeoPoint?,
        altitude: Number?,
        createdAt: Timestamp?,
        content: String?,
        userId: String?

    ) {
        this.title = title
        this.description = descriptor
        this.location = location
        this.altitude = altitude
        this.createdAt = createdAt
        this.content = content
        this.userId = userId
    }
}