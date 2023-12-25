package ie.setu.ayoeats.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface MealLocationStore {
//    fun create (mealLocation : MealLocationModel)
//    fun findAll() : ArrayList<MealLocationModel>
//
//
//    fun findById(uid : Long) : MealLocationModel?
//
//    fun delete (uid: Long)

    fun findAll(mealLocationsList:
                MutableLiveData<List<MealLocationModel>>
    )
    fun findAll(userid:String,
                mealLocationsList:
                MutableLiveData<List<MealLocationModel>>)
    fun findById(userid:String, mealLocationid: String,
                 mealLocation: MutableLiveData<MealLocationModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, mealLocation: MealLocationModel)
    fun delete(userid:String, mealLocationid: String)
    fun update(userid:String, mealLocationid: String, mealLocation: MealLocationModel)

}