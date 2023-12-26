package ie.setu.ayoeats.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location

import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import timber.log.Timber
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.Locale

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var map: GoogleMap
    var currentLocation = MutableLiveData<Location>()
    var locationClient: FusedLocationProviderClient

    val context = getApplication<Application>().applicationContext

    // User's Live Location Updates
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5000)
        .setMaxUpdateDelayMillis(15000)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            currentLocation.value = locationResult.locations.last()
        }
    }

    init {
        locationClient = LocationServices.getFusedLocationProviderClient(application)
        locationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.getMainLooper()
        )
    }

    fun updateCurrentLocation() {
        if (locationClient.lastLocation.isSuccessful)
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    currentLocation.value = location!!
                    Timber.i("MAP VM LOC SUCCESS: %s", currentLocation.value)
                }
        else // Couldn't get Last Location
            currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        Timber.i("MAP VM LOC : %s", currentLocation.value)

//        val mGeocoder = Geocoder(context, Locale.getDefault())
//
//        var addressString = ""
//
//        try {
//            val addressList: MutableList<Address>? = mGeocoder.getFromLocation(
//                currentLocation.value!!.latitude,
//                currentLocation.value!!.longitude,
//                1
//            )
//
//            // use your lat, long value here
//            if (addressList != null && addressList.isNotEmpty()) {
//                val address = addressList[0]
//                val sb = StringBuilder()
//                for (i in 0 until address.maxAddressLineIndex) {
//                    sb.append(address.getAddressLine(i)).append("\n")
//                }
//
//                // Various Parameters of an Address are appended
//                // to generate a complete Address
//                if (address.premises != null)
//                    sb.append(address.premises).append(", ")
//                sb.append(address.featureName).append(", ")
//                sb.append(address.thoroughfare).append(",")
//                sb.append(address.subAdminArea).append("\n")
//                sb.append(address.locality).append(", ")
//                sb.append(address.adminArea).append(", ")
//                sb.append(address.countryName).append(", ")
//                sb.append(address.postalCode)
//
//                // StringBuilder sb is converted into a string
//                // and this value is assigned to the
//                // initially declared addressString string.
//                addressString = sb.toString()
////                mea.address = addressString
//            }
//        } catch (e: IOException) {
//            Timber.i("An Error Occurred")
//        }
//
//        // Finally, the address string is posted in the textView with LatLng.
////        addressTV.text = "Lat: $lat \nLng: $lng \nAddress: $addressString"
//
//        Timber.i("${addressString} = address string ")
////        i("Location ${location}")
    }

    fun getLocationDetails(lat: Double , lng:Double) : String {
        val mGeocoder = Geocoder(context, Locale.getDefault())

        var addressString = ""

        try {
            val addressList: MutableList<Address>? = mGeocoder.getFromLocation(
                lat,lng,
                1
            )

            // use your lat, long value here
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append("\n")
                }

                // Various Parameters of an Address are appended
                // to generate a complete Address
                if (address.premises != null)
                    sb.append(address.premises).append(", ")
                sb.append(address.featureName).append(", ")
                sb.append(address.thoroughfare).append(",")
                sb.append(address.subAdminArea).append("\n")
                sb.append(address.locality).append(", ")
                sb.append(address.adminArea).append(", ")
                sb.append(address.countryName).append(", ")
                sb.append(address.postalCode)

                // StringBuilder sb is converted into a string
                // and this value is assigned to the
                // initially declared addressString string.
                addressString = sb.toString()
//                mea.address = addressString
            }
        } catch (e: IOException) {
            Timber.i("An Error Occurred")
        }

        // Finally, the address string is posted in the textView with LatLng.
//        addressTV.text = "Lat: $lat \nLng: $lng \nAddress: $addressString"

        Timber.i("${addressString} = address string ")
//        i("Location ${location}")
        return addressString

    }
}

