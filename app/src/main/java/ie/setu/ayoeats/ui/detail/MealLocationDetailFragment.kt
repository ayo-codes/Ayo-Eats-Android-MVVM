package ie.setu.ayoeats.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ie.setu.ayoeats.R
import ie.setu.ayoeats.databinding.FragmentMealLocationDetailBinding
import ie.setu.ayoeats.databinding.FragmentMealLocationsListBinding

class MealLocationDetailFragment : Fragment() {

    private val detailViewModel: MealLocationDetailViewModel by activityViewModels()
    private val args by navArgs<MealLocationDetailFragmentArgs>()
    private var _fragBinging : FragmentMealLocationDetailBinding? = null
    private val fragBinding get() = _fragBinging!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinging = FragmentMealLocationDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        Toast.makeText(context, "Meal Location Id selected : ${args.mealLocationuid}", Toast.LENGTH_LONG).show()

        detailViewModel.observableMealLocation.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render() {
        fragBinding.mealLocationDetail = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getMealLocation(args.mealLocationuid)
    }

}