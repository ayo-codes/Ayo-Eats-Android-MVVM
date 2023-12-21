package ie.setu.ayoeats.models

import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber.Forest.i

object MealLocationMemStore: AppCompatActivity(), MealLocationStore {

    var lastId = 0L

    internal fun getId(): Long {
        return lastId++
    }

    val mealLocations = ArrayList<MealLocationModel>()
    override fun create(mealLocation: MealLocationModel) {
        mealLocation.uid = getId()
        mealLocations.add(mealLocation)
        logAll()
    }

    override fun findAll(): ArrayList<MealLocationModel> {
        return mealLocations
    }

    override fun findById(uid:Long) : MealLocationModel ? {
        val foundMealLocation : MealLocationModel? = mealLocations.find {it.uid == uid}
        return foundMealLocation
    }

    private fun logAll() {
        mealLocations.forEach { i("$it") }
        i("$mealLocations")
    }
}