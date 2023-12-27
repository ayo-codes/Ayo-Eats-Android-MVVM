package ie.setu.ayoeats.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ie.setu.ayoeats.R
import ie.setu.ayoeats.databinding.FragmentMealLocationDetailBinding
import ie.setu.ayoeats.databinding.FragmentMealLocationsListBinding
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.ui.auth.LoggedInViewModel
import ie.setu.ayoeats.ui.mealLocation.MealLocationViewModel
import ie.setu.ayoeats.ui.mealLocationsList.MealLocationsListViewModel
import ie.setu.ayoeats.utils.customTransformation

class MealLocationDetailFragment : Fragment() {

    private val detailViewModel: MealLocationDetailViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mealLocationsListViewModel : MealLocationsListViewModel by activityViewModels()
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

        fragBinding.editMealLocationButton.setOnClickListener {
            detailViewModel.updateMealLocation(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.mealLocationuid , fragBinding.mealLocationDetail?.observableMealLocation!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteMealLocationButton.setOnClickListener {
            mealLocationsListViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.uid!! , detailViewModel.observableMealLocation.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
        fragBinding.mealLocationDetail = detailViewModel
        Picasso.get().load(detailViewModel.observableMealLocation.value?.mealImage?.toUri())
            .resize(200, 200)
            .transform(customTransformation())
            .centerCrop()
            .into(fragBinding.mealLocationImage)
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getMealLocation(loggedInViewModel.liveFirebaseUser.value?.uid!! ,args.mealLocationuid)
    }

}