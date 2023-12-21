package ie.setu.ayoeats.ui.mealLocationsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.ayoeats.adapters.MealLocationAdapter
import ie.setu.ayoeats.adapters.MealLocationListener
import ie.setu.ayoeats.databinding.FragmentMealLocationsListBinding
import ie.setu.ayoeats.models.MealLocationModel
import timber.log.Timber


class MealLocationsListFragment : Fragment() , MealLocationListener {

    private var _fragBinding: FragmentMealLocationsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private val mealLocationsListViewModel : MealLocationsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val mealLocationsListViewModel = ViewModelProvider(this).get(MealLocationsListViewModel::class.java)

        _fragBinding = FragmentMealLocationsListBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        mealLocationsListViewModel.observableMealLocationsList.observe(viewLifecycleOwner , Observer {
            mealLocations ->
            mealLocations?.let {
                render( mealLocations as ArrayList<MealLocationModel>)
            }
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

    override fun onMealLocationClick(mealLocationModel: MealLocationModel) {
        Timber.i("Clicked the meal location : $mealLocationModel")
    }

    override fun onResume() {
        super.onResume()
        mealLocationsListViewModel.loadAll()
    }


}