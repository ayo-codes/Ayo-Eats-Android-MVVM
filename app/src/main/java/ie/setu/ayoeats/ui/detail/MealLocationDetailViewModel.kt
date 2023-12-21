package ie.setu.ayoeats.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.ayoeats.models.MealLocationMemStore
import ie.setu.ayoeats.models.MealLocationModel

class MealLocationDetailViewModel : ViewModel() {
    private val mealLocation = MutableLiveData<MealLocationModel>()

    val observableMealLocation : LiveData<MealLocationModel>
        get() = mealLocation

    fun getMealLocation(uid : Long) {
        mealLocation.value = MealLocationMemStore.findById(uid)
    }
}