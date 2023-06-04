package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val AUTH_CODE = 1001
    private lateinit var binding: ActivityAuthenticationBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)


        //authButton signInflow init listener
        binding.authButton.setOnClickListener { launchSignInFlow() }
        viewModel.authState.observe(this) { state ->
            //if auth is successful, navigate to RemindersActivity
            when (state) {
                AuthViewModel.AuthState.AUTHENTICATED -> navigateActivities()
                else -> Log.i("authenticationState", "$state")
            }
        }
    }


    private fun launchSignInFlow() {
        //sign-in with email or gmail
        val authProviders = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(authProviders).build(), AUTH_CODE
        )
    }

    private fun navigateActivities() {
        //if auth is successful, navigate to RemindersActivity
        val navIntent = Intent(this, RemindersActivity::class.java)
        startActivity(navIntent)
    }
}