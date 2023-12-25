package ie.setu.ayoeats.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        database.child("user-meal-locations").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase MealLocation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<MealLocationModel>()
                    val children = snapshot.children
                    children.forEach {
                        val mealLocation = it.getValue(MealLocationModel::class.java)
                        localList.add(mealLocation!!)
                    }
                    database.child("user-meal-locations").child(userid)
                        .removeEventListener(this)

                    mealLocationsList.value = localList
                }
            })
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
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/meal-locations/$mealLocationid"] = null
        childDelete["/user-meal-locations/$userid/$mealLocationid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, mealLocationid: String, mealLocation: MealLocationModel) {
        TODO("Not yet implemented")
    }
}