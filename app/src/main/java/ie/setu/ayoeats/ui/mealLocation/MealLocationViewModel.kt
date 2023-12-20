package ie.setu.ayoeats.ui.mealLocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MealLocationViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text

    private val status = MutableLiveData<Boolean>()

    val observableStatus : LiveData<Boolean>
        get() = status

    fun addMealLocation(){
        status.value = try {
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}