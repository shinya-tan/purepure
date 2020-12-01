package com.example.tabi_tabi.model

import android.net.Uri
import java.io.Serializable

class UserModel : Serializable {
  var name: String? = null
  var email: String? = null
  var uid: String? = null
  var icon: String? = null


  constructor()
  constructor(
    name: String?,
    email: String?,
    uid: String?,
    icon: String?

  ) {
    this.name = name
    this.email = email
    this.uid = uid
    this.icon = icon

  }
}