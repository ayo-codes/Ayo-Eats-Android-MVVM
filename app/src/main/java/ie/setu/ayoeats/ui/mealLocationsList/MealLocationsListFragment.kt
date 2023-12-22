package ie.setu.ayoeats.ui.mealLocationsList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.ayoeats.adapters.MealLocationAdapter
import ie.setu.ayoeats.adapters.MealLocationListener
import ie.setu.ayoeats.databinding.FragmentMealLocationsListBinding
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.utils.createLoader
import ie.setu.ayoeats.utils.hideLoader
import ie.setu.ayoeats.utils.showLoader
import timber.log.Timber


class MealLocationsListFragment : Fragment() , MealLocationListener {

    private var _fragBinding: FragmentMealLocationsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private val mealLocationsListViewModel : MealLocationsListViewModel by activityViewModels()

    lateinit var loader : AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val mealLocationsListViewModel = ViewModelProvider(this).get(MealLocationsListViewModel::class.java)

        _fragBinding = FragmentMealLocationsListBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        loader = createLoader(requireActivity()) // creates the loader icon

        showLoader(loader, "Fetching your list of Meal Locations ")
        mealLocationsListViewModel.observableMealLocationsList.observe(viewLifecycleOwner , Observer {
            mealLocations ->
            mealLocations?.let {
                render( mealLocations as ArrayList<MealLocationModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }

            setSwipeRefresh()
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun render (mealLocationsList : ArrayList<MealLocationModel>){
        fragBinding.recyclerView.adapter = MealLocationAdapter(mealLocationsList , this )
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
        val action = MealLocationsListFragmentDirections.actionNavHomeToMealLocationDetailFragment(mealLocation.uid)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, " Fetching your meal locations ")
//        mealLocationsListViewModel.loadAll()
        mealLocationsListViewModel.observableMealLocationsList.observe(viewLifecycleOwner , Observer {
                mealLocations ->
            mealLocations?.let {
                render( mealLocations as ArrayList<MealLocationModel>)
                hideLoader(loader)
            }
        })
    }

    // Refresh the feed
    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader, "Fetching your meal locations")
        }
    }

    fun checkSwipeRefresh() {
        if(fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }


}