package ie.setu.ayoeats.ui.mealLocation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ie.setu.ayoeats.R
import ie.setu.ayoeats.databinding.FragmentMealLocationBinding
import ie.setu.ayoeats.firebase.FirebaseImageManager
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.ui.auth.LoggedInViewModel
import ie.setu.ayoeats.ui.map.MapsViewModel
import ie.setu.ayoeats.utils.readImageUri
import ie.setu.ayoeats.utils.showImagePicker
import timber.log.Timber

class MealLocationFragment : Fragment() {

    private var _fragBinding: FragmentMealLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private val mealLocationViewModel: MealLocationViewModel by activityViewModels()// view model
    private val loggedInViewModel: LoggedInViewModel by activityViewModels() // logged in user details
    private val mapsViewModel: MapsViewModel by activityViewModels() // for maps

    private lateinit var intentLauncher : ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _fragBinding = FragmentMealLocationBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root

//        mealLocationViewModel =
//            ViewModelProvider(this).get(MealLocationViewModel::class.java) // initialise the view model - not reqd as using activityviewmodels

        mealLocationViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })

        fragBinding.seekBarRatings.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                fragBinding.RatingsProgress.text =
                    fragBinding.seekBarRatings.progress.toString() + " Stars"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                fragBinding.RatingsProgress.text = fragBinding.seekBarRatings.progress.toString()
            }
        })

        setAddDonationButtonListener(fragBinding)


//        val textView: TextView = binding.textGallery
//        mealLocationViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        fragBinding.mealLocationImage.setOnClickListener {
            Snackbar.make(it,"Click here to add image ", Snackbar.LENGTH_LONG)
                .show()
            showImagePicker(intentLauncher)
        }

        registerMealImagePickerCallback()
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

    fun setAddDonationButtonListener(layout: FragmentMealLocationBinding) {
        layout.btnAdd.setOnClickListener {

            if (layout.mealName.text.toString().isEmpty()) {
                Snackbar.make(it, R.string.enter_mealLocation_mealName, Snackbar.LENGTH_LONG)
                    .show() // This shows the warning message if the field is empty
            } else {
                val mealName = layout.mealName.text.toString()
                val mealDescription = layout.mealDescription.text.toString()
                val mealPrice = layout.mealPrice.text.toString().toDouble()
                val mealRating = layout.RatingsProgress.text.toString().toDouble()

                mealLocationViewModel.addMealLocation(
                    loggedInViewModel.liveFirebaseUser,
                    MealLocationModel(
                        mealName = mealName,
                        mealDescription = mealDescription,
                        mealPrice = mealPrice,
                        mealRating = mealRating,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                        latitude = mapsViewModel.currentLocation.value!!.latitude,
                        longitude = mapsViewModel.currentLocation.value!!.longitude,
                        address = mapsViewModel.getLocationDetails(mapsViewModel.currentLocation.value!!.latitude, mapsViewModel.currentLocation.value!!.longitude)


                    )
                )

                Snackbar.make(it, R.string.meal_location_add_successful, Snackbar.LENGTH_LONG)
                    .show() // This shows the warning message if the field is empty
                Timber.i("Click Worked")
                Timber.i(mealName)

                layout.mealName.text.clear()
                layout.mealDescription.text.clear()
                layout.mealPrice.text.clear()

                findNavController().navigateUp()
            }
        }
    }

    private fun registerMealImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateMealImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    fragBinding.mealLocationImage,
                                    true)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }



}

