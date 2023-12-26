package ie.setu.ayoeats.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import ie.setu.ayoeats.R
import ie.setu.ayoeats.databinding.ActivityMainBinding
import ie.setu.ayoeats.databinding.NavHeaderMainBinding
import ie.setu.ayoeats.firebase.FirebaseImageManager
import ie.setu.ayoeats.ui.auth.LoggedInViewModel
import ie.setu.ayoeats.ui.auth.Login
import ie.setu.ayoeats.ui.map.MapsViewModel
import ie.setu.ayoeats.utils.checkLocationPermissions
import ie.setu.ayoeats.utils.customTransformation
import ie.setu.ayoeats.utils.isPermissionGranted
import ie.setu.ayoeats.utils.readImageUri
import ie.setu.ayoeats.utils.showImagePicker
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var homeBinding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var navHeaderBinding : NavHeaderMainBinding
    private lateinit var headerView : View // header view
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private val mapsViewModel : MapsViewModel by viewModels() // import maps view models

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding to main activity
        homeBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        drawerLayout = homeBinding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.BLACK) // change color of text

        val navController = findNavController(R.id.nav_host_fragment_content_main)

//        homeBinding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val navView: NavigationView = homeBinding.navView
        navView.setupWithNavController(navController)



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_meal_location, R.id.mapsFragment, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        initNavHeader() // Sets up the navigation header

        // check location permissions
        if(checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }

    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser !=null)
//                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
                updateNavHeader(firebaseUser) // passing in the firebaseUser from the loggedInView
        })

        // if the user is logged out
        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if(loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

        // picker for profile image
        registerImagePickerCallback()
    }

    private fun updateNavHeader(currentUser: FirebaseUser){
        FirebaseImageManager.imageUri.observe(this) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("AE NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.navHeaderImage,
                        false
                    )
                } else {
                    Timber.i("AE Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.unknown_person,
                        navHeaderBinding.navHeaderImage
                    )
                }        } else // load existing image from firebase
            {
                Timber.i("AE Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.navHeaderImage, false
                )
            }    }
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if(currentUser.displayName != null)
            navHeaderBinding.navHeaderName.text = currentUser.displayName




//        navHeaderBinding.navHeaderEmail.text = currentUser.email
//        if(currentUser.photoUrl != null && currentUser.displayName != null) {
//            navHeaderBinding.navHeaderName.text = currentUser.displayName
//            Picasso.get().load(currentUser.photoUrl)
//                .resize(200, 200)
//                .transform(customTransformation())
//                .centerCrop()
//                .into(navHeaderBinding.navHeaderImage)
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderMainBinding.bind(headerView)
        navHeaderBinding.navHeaderImage.setOnClickListener {
            Toast.makeText(this, "Click to Change Image", Toast.LENGTH_SHORT).show()
            showImagePicker(intentLauncher)
        }
    }

    // images
    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderBinding.navHeaderImage,
                                    true)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    //Permissions for Location
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }




}


