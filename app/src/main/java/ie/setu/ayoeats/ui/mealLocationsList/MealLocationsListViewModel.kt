package ie.setu.ayoeats.ui.mealLocationsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.ayoeats.firebase.FirebaseDBManager
import ie.setu.ayoeats.models.MealLocationModel
import timber.log.Timber

class MealLocationsListViewModel : ViewModel() {

    private val mealLocationsList = MutableLiveData<List<MealLocationModel>>()

    val observableMealLocationsList: LiveData<List<MealLocationModel>>
        get() = mealLocationsList

    val liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false) // added to determine who owns a meal location

    init {

        load()
    }

    fun load(){
        try {
//           mealLocationsList.value = MealLocationMemStore.findAll()
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!! , mealLocationsList)
            Timber.i("Loading items : ${mealLocationsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Load Error : ${e.message}")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("MealLocation Delete Success")
        }
        catch (e: Exception) {
            Timber.i("MealLocation  Delete Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(mealLocationsList)
            Timber.i("Meal Locations LoadAll Success : ${mealLocationsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Meal Locations  LoadAll Error : $e.message")
        }
    }



}