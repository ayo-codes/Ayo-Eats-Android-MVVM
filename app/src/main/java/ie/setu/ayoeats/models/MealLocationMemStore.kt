package ie.setu.ayoeats.models

import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber.Forest.i

object MealLocationMemStore: AppCompatActivity(), MealLocationStore {

    val mealLocations = ArrayList<MealLocationModel>()
    override fun create(mealLocation: MealLocationModel) {
        mealLocations.add(mealLocation)
        logAll()
    }

    override fun findAll(): ArrayList<MealLocationModel> {
        return mealLocations
    }

    private fun logAll() {
        mealLocations.forEach { i("$it") }
        i("$mealLocations")
    }
}