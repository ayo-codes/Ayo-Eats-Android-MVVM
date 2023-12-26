package ie.setu.ayoeats.ui.mealLocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.ayoeats.firebase.FirebaseDBManager
import ie.setu.ayoeats.firebase.FirebaseImageManager
import ie.setu.ayoeats.models.MealLocationMemStore
import ie.setu.ayoeats.models.MealLocationModel

class MealLocationViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text

    private val status = MutableLiveData<Boolean>()

    val observableStatus : LiveData<Boolean>
        get() = status

//    val mealLocations = MealLocationMemStore
    fun addMealLocation(firebaseUser: MutableLiveData<FirebaseUser> , mealLocation : MealLocationModel){
        status.value = try {
//            MealLocationMemStore.create(mealLocation)
            mealLocation.profilepic = FirebaseImageManager.imageUri.value.toString()
            mealLocation.mealImage = FirebaseImageManager.mealImageUri.value.toString() // Add meal image
            FirebaseDBManager.create(firebaseUser, mealLocation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }


}