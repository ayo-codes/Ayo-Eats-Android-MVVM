package ie.setu.ayoeats.models

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.Forest.i

object MealLocationMemStore: AppCompatActivity(), MealLocationStore {

    var lastId = 0L

    internal fun getId(): Long {
        return lastId++
    }

    val mealLocations = ArrayList<MealLocationModel>()
    fun create(mealLocation: MealLocationModel) {
//        mealLocation.uid = getId()
        mealLocations.add(mealLocation)
        logAll()
    }

     fun findAll(): ArrayList<MealLocationModel> {
        return mealLocations
    }

     fun findById(uid:String) : MealLocationModel ? {
        val foundMealLocation : MealLocationModel? = mealLocations.find {it.uid == uid}
        return foundMealLocation
    }

     fun delete(uid: Long) {
        TODO("Not yet implemented")
    }

    private fun logAll() {
        mealLocations.forEach { i("$it") }
        i("$mealLocations")
    }

    override fun findAll(mealLocationsList: MutableLiveData<List<MealLocationModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(
        userid: String,
        mealLocationsList: MutableLiveData<List<MealLocationModel>>
    ) {
        TODO("Not yet implemented")
    }

    override fun findById(
        userid: String,
        mealLocationid: String,
        mealLocation: MutableLiveData<MealLocationModel>
    ) {
        TODO("Not yet implemented")
    }

    override fun create(
        firebaseUser: MutableLiveData<FirebaseUser>,
        mealLocation: MealLocationModel
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, mealLocationid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, mealLocationid: String, mealLocation: MealLocationModel) {
        TODO("Not yet implemented")
    }
}