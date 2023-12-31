package ie.setu.ayoeats.ui.mealLocationsList

import android.app.AlertDialog
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.ayoeats.R
import ie.setu.ayoeats.adapters.MealLocationAdapter
import ie.setu.ayoeats.adapters.MealLocationListener
import ie.setu.ayoeats.databinding.FragmentMealLocationsListBinding
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.ui.auth.LoggedInViewModel
import ie.setu.ayoeats.utils.SwipeToDeleteCallback
import ie.setu.ayoeats.utils.SwipeToEditCallback
import ie.setu.ayoeats.utils.createLoader
import ie.setu.ayoeats.utils.hideLoader
import ie.setu.ayoeats.utils.showLoader
import timber.log.Timber


class MealLocationsListFragment : Fragment(), MealLocationListener {

    private var _fragBinding: FragmentMealLocationsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private val mealLocationsListViewModel: MealLocationsListViewModel by activityViewModels() // bring in meal locations list model
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()


    lateinit var loader: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val mealLocationsListViewModel = ViewModelProvider(this).get(MealLocationsListViewModel::class.java)

        _fragBinding = FragmentMealLocationsListBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root

        setupMenu() // Calls the setup menu

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        fragBinding.fab.setOnClickListener {
            val action = MealLocationsListFragmentDirections.actionNavHomeToNavMealLocation()
            findNavController().navigate(action)
        }

        loader = createLoader(requireActivity()) // creates the loader icon

        showLoader(loader, "Fetching your list of Meal Locations ")
        mealLocationsListViewModel.observableMealLocationsList.observe(
            viewLifecycleOwner,
            Observer { mealLocations ->
                mealLocations?.let {
                    render(mealLocations as ArrayList<MealLocationModel>)
                    hideLoader(loader)
                    checkSwipeRefresh()
                }


            })

        setSwipeRefresh()

        //swipe delete
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader, "Deleting Meal Location")
                val adapter =
                    fragBinding.recyclerView.adapter as MealLocationAdapter // sets adapter to the meallocation adapter
                adapter.removeAt(viewHolder.adapterPosition) // calls the removeAt function in the recycler view
                mealLocationsListViewModel.delete(mealLocationsListViewModel.liveFirebaseUser.value?.uid!!, (viewHolder.itemView.tag as MealLocationModel).uid!!)
                hideLoader(loader)

            }
        }

        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        // swipe Edit
        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onMealLocationClick(viewHolder.itemView.tag as MealLocationModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun render(mealLocationsList: ArrayList<MealLocationModel>) {
        fragBinding.recyclerView.adapter = MealLocationAdapter(mealLocationsList, this , mealLocationsListViewModel.readOnly.value!!)
        if (mealLocationsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.mealLocationsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.mealLocationsNotFound.visibility = View.GONE
        }

    }

    override fun onMealLocationClick(mealLocation: MealLocationModel) {
        Timber.i("Clicked the meal location : $mealLocation")
        val action = MealLocationsListFragmentDirections.actionNavHomeToMealLocationDetailFragment(
            mealLocation.uid!!
        )
        if(!mealLocationsListViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, " Fetching your meal locations ")
//        mealLocationsListViewModel.loadAll()
//        mealLocationsListViewModel.observableMealLocationsList.observe(
//            viewLifecycleOwner,
//            Observer { mealLocations ->
//                mealLocations?.let {
//                    render(mealLocations as ArrayList<MealLocationModel>)
//                    hideLoader(loader)
//                }
//            })
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                mealLocationsListViewModel.liveFirebaseUser.value = firebaseUser
                mealLocationsListViewModel.load()
            }
        })
//        hideLoader(loader)
    }

    // Refresh the feed
    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader, "Fetching your meal locations")
            if (mealLocationsListViewModel.readOnly.value!!)
                mealLocationsListViewModel.loadAll()
            else
                mealLocationsListViewModel.load()
        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }


    // Menu Setup

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)

                val item = menu.findItem(R.id.toggleDonations) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleDonations: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleDonations.isChecked = false

                toggleDonations.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) mealLocationsListViewModel.loadAll()
                    else mealLocationsListViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}