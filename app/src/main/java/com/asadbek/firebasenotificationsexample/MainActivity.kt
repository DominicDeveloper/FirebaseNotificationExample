package com.asadbek.firebasenotificationsexample

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.asadbek.firebasenotificationsexample.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Owner: @Dominic_Azimov
 * Steps:{
 *
 *        Permission: Post notification
 *        Binding
 *        Firebase connection, messaging service
 *        metada
 *        2 functions requestPermission and requestPermissionLauncher
 *        }
 */
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener {
            requestPermission()
        }
    }

    fun requestPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }else{
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                // Yangi FCM registratsiya tokenini olish
                val token = task.result
                Log.d(TAG, "requestPermission: $token")
            })

        }

    }
    // Declare the launcher at the top of your Activity/Fragment:
    @SuppressLint("StringFormatInvalid")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            // FCM SDK (va sizning dastur) bildirishnomalarni  ko`rsata oladi if isGranted == true
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MyError", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                // Yangi FCM registratsiya tokenini olish
                val token = task.result
                Log.d("MyToken", "FirebaseToken: $token")

            })
        } else {
            // Foydalanuvchiga ilovangiz bildirishnomalarni ko'rsatmasligi haqida xabar bering.

        }
    }
}