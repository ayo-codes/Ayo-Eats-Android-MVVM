package ie.setu.ayoeats.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ie.setu.ayoeats.R

class MealLocationDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MealLocationDetailFragment()
    }

    private lateinit var viewModel: MealLocationDetailViewModel
    private val args by navArgs<MealLocationDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_location_detail, container, false)

        Toast.makeText(context, "Meal Location Id selected : ${args.mealLocationuid}", Toast.LENGTH_LONG).show()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MealLocationDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}