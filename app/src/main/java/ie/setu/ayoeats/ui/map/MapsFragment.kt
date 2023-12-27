package ie.setu.ayoeats.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.ayoeats.R
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.ui.auth.LoggedInViewModel
import ie.setu.ayoeats.ui.mealLocationsList.MealLocationsListViewModel
import ie.setu.ayoeats.utils.createLoader
import ie.setu.ayoeats.utils.hideLoader
import ie.setu.ayoeats.utils.showLoader
import com.google.android.libraries.places.api.Places
import com.google.android.material.snackbar.Snackbar

class MapsFragment: Fragment()  {

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val mealLocationListViewModel: MealLocationsListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    lateinit var loader: AlertDialog


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
//            mapsViewModel.getLocationDetails(mapsViewModel.currentLocation.value!!.latitude, mapsViewModel.currentLocation.value!!.longitude)
//            mapsViewModel.map.addMarker(MarkerOptions().position(loc).title("You are Here!"))


            mealLocationListViewModel.observableMealLocationsList.observe(
                viewLifecycleOwner, Observer { mealLocations ->
                    mealLocations?.let {
                        render(mealLocations as ArrayList<MealLocationModel>)
                        hideLoader(loader)
                    }
                }
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())
        setupMenu()
        Places.initialize(mapsViewModel.context, "@string/google_api_key")
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    // render for meal locations
    private fun render(mealLocationsList: ArrayList<MealLocationModel>) {
        var markerColour: Float
        if (mealLocationsList.isNotEmpty()) {
            mapsViewModel.map.clear()
            mealLocationsList.forEach {
                markerColour =
                    if (it.email.equals(this.mealLocationListViewModel.liveFirebaseUser.value!!.email))
                        BitmapDescriptorFactory.HUE_AZURE + 5
                    else
                        BitmapDescriptorFactory.HUE_RED

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.mealName} ${it.mealDescription}")
                        .snippet(it.mealPrice.toString())
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(markerColour)
                        )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, "Fetching Meal Locations")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                mealLocationListViewModel.liveFirebaseUser.value = firebaseUser
                mealLocationListViewModel.load()
            }
        }
    }

    // Menu
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)

                val item = menu.findItem(R.id.toggleDonations) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleDonations: SwitchCompat =
                    item.actionView!!.findViewById(R.id.toggleButton)
                toggleDonations.isChecked = false

                toggleDonations.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) mealLocationListViewModel.loadAll()
                    else mealLocationListViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}

