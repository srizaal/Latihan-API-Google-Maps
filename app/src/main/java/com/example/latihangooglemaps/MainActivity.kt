package com.example.latihangooglemaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mLocationRequest: LocationRequest
    lateinit var mMap: GoogleMap

    var mLastLocation: Location? = null
    var mCurrentLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null

    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val locationList = locationResult?.locations
            if (locationList!!.isNotEmpty()) {
                val location = locationList.last()
                Log.i("MyLocation", location.toString())

                mLastLocation = location
                if (mCurrentLocationMarker != null)
                {
                    mCurrentLocationMarker?.remove()
                }
                //Letakkan marker pada posisi kita yang terbaru
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Posisi sekarang "
                        +location.latitude.toString()
                        +","+location.longitude.toString())
                markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))

                //Men-attach markerOptions ke mMap
                mCurrentLocationMarker = mMap.addMarker(markerOptions)

                //Memfokuskan camera ke posisi saat ini
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0F))


            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
//        val decatlon = LatLng(-6.2287684, 106.9954052)
//        val candrabaga = LatLng(-6.23871593313, 106.992657926)
//        val cevest = LatLng(-6.235371, 106.989905)
//        val mm = LatLng(-6.2478, 106.98943)
//        val mitra = LatLng(-6.292596, 107.151435)
//
//
//        googleMap.addMarker(
//            MarkerOptions().position(cevest).title("Marker in Cevest")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(decatlon).title("Marker in decatlon")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(candrabaga).title("Marker in candrabaga")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(mm).title("Marker in mm")
//        )
//        googleMap.addMarker(
//            MarkerOptions().position(mitra).title("Marker in mitra")
//        )


        //ZOOM IN
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cevest, 15f))
//
//        }
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 20000

        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return Toast.makeText(this, "Izinkan penggunaan akses lokasi",
            Toast.LENGTH_LONG).show()
        }

        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
        mMap.isMyLocationEnabled = true
    }
}