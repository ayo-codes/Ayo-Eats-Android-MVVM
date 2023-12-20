package ie.setu.ayoeats.ui.mealLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ie.setu.ayoeats.R
import ie.setu.ayoeats.databinding.FragmentMealLocationBinding
import ie.setu.ayoeats.models.MealLocationModel
import timber.log.Timber

class MealLocationFragment : Fragment() {

    private var _fragBinding: FragmentMealLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var mealLocationViewModel: MealLocationViewModel // view model



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _fragBinding = FragmentMealLocationBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root

        mealLocationViewModel =
            ViewModelProvider(this).get(MealLocationViewModel::class.java) // initialise the view model

        mealLocationViewModel.observableStatus.observe(viewLifecycleOwner , Observer { status ->
            status?.let { render(status) }
        })

        setAddDonationButtonListener(fragBinding)


//        val textView: TextView = binding.textGallery
//        mealLocationViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                }
            }
            false -> {
                Toast.makeText(context, "Error : Could not Add Meal Location..", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun setAddDonationButtonListener(layout: FragmentMealLocationBinding ){
        layout.btnAdd.setOnClickListener{

            if (layout.mealName.text.toString().isEmpty()) {
               Snackbar.make(it, R.string.enter_mealLocation_mealName, Snackbar.LENGTH_LONG).show() // This shows the warning message if the field is empty
            } else {
                var mealName = layout.mealName.text.toString()
                mealLocationViewModel.addMealLocation(MealLocationModel(mealName =  mealName , mealDescription = "Testing this "))

                Timber.i("Click Worked")
                Timber.i(mealName)
            }
        }
    }
}

