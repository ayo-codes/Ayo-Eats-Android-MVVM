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
        database.child("meal-locations")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase MealLocation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<MealLocationModel>()
                    val children = snapshot.children
                    children.forEach {
                        val donation = it.getValue(MealLocationModel::class.java)
                        localList.add(donation!!)
                    }
                    database.child("meal-locations")
                        .removeEventListener(this)

                    mealLocationsList.value = localList
                }
            })
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
        database.child("user-meal-locations").child(userid)
            .child(mealLocationid).get().addOnSuccessListener {
                mealLocation.value = it.getValue(MealLocationModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
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
        val mealLocationValues = mealLocation.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["meal-locations/$mealLocationid"] = mealLocationValues
        childUpdate["user-meal-locations/$userid/$mealLocationid"] = mealLocationValues

        database.updateChildren(childUpdate)
    }

    // manages the update of a user's profile photo
    fun updateImageRef(userid: String,imageUri: String) {

        val userMealLocations = database.child("user-meal-locations").child(userid)
        val allMealLocations = database.child("meal-locations")

        userMealLocations.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all meal locations that match 'it'
                        val mealLocation = it.getValue(MealLocationModel::class.java)
                        allMealLocations.child(mealLocation!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}