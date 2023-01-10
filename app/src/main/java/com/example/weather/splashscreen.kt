package com.example.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weather.databinding.ActivitySplashscreenBinding
import com.google.android.gms.location.*


class splashscreen : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myResquestCode=1010
    private lateinit var binding: ActivitySplashscreenBinding
    //@RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

       getlastlocation()

    }

    @SuppressLint("MissingPermission")
   // @RequiresApi(Build.VERSION_CODES.N)
    private fun getlastlocation() {
       if (checkpermission())
       {
           if (LocationEnabled()){

               fusedLocationClient.lastLocation
                   .addOnSuccessListener {

                       val currentLocation : Location? = it

                       if (currentLocation == null ){
                           NewLocation()
                       }
                       else{
                           Handler(Looper.getMainLooper()).postDelayed({
                               val intent= Intent(this, MainActivity::class.java)
            intent.putExtra("lat",currentLocation.latitude.toString())
            intent.putExtra("long",currentLocation.longitude.toString())
                               startActivity(intent)
                               finish()
                           },2000)
                           Log.i("Location",currentLocation.longitude.toString())
                       }
                   }
           }
           else
           { RequestLocation() }
       }
       else   {  requestpermission() }



}


    private fun checkpermission(): Boolean {
//        if (
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
     //   return false


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true

    }


    @SuppressLint("MissingPermission")
    private fun NewLocation() {
        val locationRequest = LocationRequest.create().apply{
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates=1

        }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@splashscreen)

            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

}
    private val locationCallback=object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location? =p0.lastLocation
        }
    }

    private fun RequestLocation() {

        Toast.makeText(this,"open your location",Toast.LENGTH_LONG).show()
    }

    private fun LocationEnabled(): Boolean {
        val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    private fun requestpermission() {


        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,

                Manifest.permission.INTERNET,

            ), myResquestCode
        )

//        val locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                    // Precise location access granted.
//                }
//                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                    // Only approximate location access granted.
//                } else -> {
//                Toast.makeText(this,"we cannot show weather without permission ",Toast.LENGTH_LONG).show()
//            }
//            }
//        }
//        locationPermissionRequest.launch(arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION))
//    }

    }

       // @RequiresApi(Build.VERSION_CODES.N)
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
        ) {


            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (requestCode == myResquestCode) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getlastlocation()
                }
            }


//        when (requestCode) {
//           myResquestCode -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() &&
//                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // Permission is granted. Continue the action or workflow
//                    // in your app.
//                    getlastlocation()
//                } else {
//                    Toast.makeText(this,"we cannot show weather without permission ",Toast.LENGTH_LONG).show()
//                }
////                return getlastlocation()
//            }
//
//            // Add other 'when' lines to check for other
//            // permissions this app might request.
//            else -> {
//                requestpermission()
//                // Ignore all other requests.
//            }
//        }


    }
}