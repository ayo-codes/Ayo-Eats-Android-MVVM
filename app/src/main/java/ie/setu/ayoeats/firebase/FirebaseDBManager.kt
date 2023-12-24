package ie.setu.ayoeats.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.models.MealLocationStore
import timber.log.Timber

object FirebaseDBManager : MealLocationStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
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
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("meal-locations").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        mealLocation.uid = key
        val meallocationValues = mealLocation.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/meal-locations/$key"] = meallocationValues
        childAdd["/user-meal-locations/$uid/$key"] = meallocationValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, mealLocationid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, mealLocationid: String, mealLocation: MealLocationModel) {
        TODO("Not yet implemented")
    }
}