package ie.setu.ayoeats.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.ayoeats.firebase.FirebaseDBManager
import ie.setu.ayoeats.models.MealLocationMemStore
import ie.setu.ayoeats.models.MealLocationModel
import timber.log.Timber

class MealLocationDetailViewModel : ViewModel() {
    private val mealLocation = MutableLiveData<MealLocationModel>()

    var observableMealLocation : LiveData<MealLocationModel>
        get() = mealLocation
        set(value) {mealLocation.value = value.value}

    fun getMealLocation( userid: String,id : String ) {
        try {
            FirebaseDBManager.findById(userid, id, mealLocation)
            Timber.i("Detail getMealLocation() Success : ${
                mealLocation.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getMealLocation() Error : $e.message")
        }
    }

    fun updateMealLocation(userid:String, id: String, mealLocation: MealLocationModel) {
        try {
            FirebaseDBManager.update(userid, id, mealLocation)
            Timber.i("Detail update() Success : $mealLocation")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }

}