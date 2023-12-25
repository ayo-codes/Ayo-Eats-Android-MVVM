package ie.setu.ayoeats.ui.mealLocationsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.ayoeats.firebase.FirebaseDBManager
import ie.setu.ayoeats.models.MealLocationMemStore
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.models.MealLocationStore
import timber.log.Timber

class MealLocationsListViewModel : ViewModel() {

    private val mealLocationsList = MutableLiveData<List<MealLocationModel>>()

    val observableMealLocationsList: LiveData<List<MealLocationModel>>
        get() = mealLocationsList

    val liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {

        loadAll()
    }

    fun loadAll(){
        try {
//           mealLocationsList.value = MealLocationMemStore.findAll()
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!! , mealLocationsList)
            Timber.i("Loading items : ${mealLocationsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Load Error : ${e.message}")
        }
    }


}