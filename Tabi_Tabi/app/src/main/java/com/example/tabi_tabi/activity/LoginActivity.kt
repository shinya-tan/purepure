package com.example.tabi_tabi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tabi_tabi.R
import com.example.tabi_tabi.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth
  var db: FirebaseFirestore? = null

  private lateinit var googleSignInClient: GoogleSignInClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    signInButton.setOnClickListener {
      signIn()
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build()

    googleSignInClient = GoogleSignIn.getClient(this, gso)

    auth = Firebase.auth

    if (auth.currentUser != null) {
      val intent = Intent(applicationContext, MainActivity::class.java)
      startActivity(intent)
      finish()
    }

  }

  override fun onStart() {
    super.onStart()
    val currentUser = auth.currentUser
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == RC_SIGN_IN) {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      try {
        val account = task.getResult(ApiException::class.java)!!
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
        firebaseAuthWithGoogle(account.idToken!!)
      } catch (e: ApiException) {
        Log.w(TAG, "Google sign in failed", e)
        // [START_EXCLUDE]
      }
    }
  }

  private fun firebaseAuthWithGoogle(idToken: String) {

    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          val user = UserModel(
            auth.currentUser!!.displayName,
            auth.currentUser!!.email,
            auth.currentUser!!.uid,
            auth.currentUser!!.photoUrl.toString()
          )
          this.db = FirebaseFirestore.getInstance()
          db!!.collection("users").whereEqualTo("uid", auth.uid).get()
            .addOnSuccessListener {
              val intent = Intent(applicationContext, MainActivity::class.java)
              startActivity(intent)
              finish()
            }
            .addOnFailureListener {
              db!!.collection("users")
                .document()
                .set(user)
                .addOnSuccessListener {
                  Log.d(TAG, "signInWithCredential:success")
                  Toast.makeText(this, "Login成功", Toast.LENGTH_SHORT).show()
                  val intent = Intent(applicationContext, MainActivity::class.java)
                  startActivity(intent)
                  finish()
                }
                .addOnFailureListener {
                }
            }

        } else {
          Log.w(TAG, "signInWithCredential:failure", task.exception)
          Toast.makeText(this, "Login失敗", Toast.LENGTH_SHORT).show()
        }
      }
  }

  private fun signIn() {
    val signInIntent = googleSignInClient.signInIntent
    startActivityForResult(signInIntent, RC_SIGN_IN)
  }

  companion object {
    private const val TAG = "LoginActivity"
    private const val RC_SIGN_IN = 1
  }
}