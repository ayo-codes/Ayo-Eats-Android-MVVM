package ie.setu.ayoeats.ui.mealLocationsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.ayoeats.models.MealLocationMemStore
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.models.MealLocationStore
import timber.log.Timber

class MealLocationsListViewModel : ViewModel() {

    private val mealLocationsList = MutableLiveData<ArrayList<MealLocationModel>>()

    val observableMealLocationsList: LiveData<ArrayList<MealLocationModel>>
        get() = mealLocationsList

    init {

        loadAll()
    }

    fun loadAll(){
        try {
           mealLocationsList.value = MealLocationMemStore.findAll()
            Timber.i("Loading items : ${mealLocationsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Load Error : ${e.message}")
        }
    }
}