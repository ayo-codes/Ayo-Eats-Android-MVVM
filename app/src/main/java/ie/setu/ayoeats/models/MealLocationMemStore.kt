package ie.setu.ayoeats.models

import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber.Forest.i

object MealLocationMemStore: AppCompatActivity(), MealLocationStore {

    val mealLocations = ArrayList<MealLocationModel>()
    override fun create(mealLocation: MealLocationModel) {
        mealLocations.add(mealLocation)
        logAll()
    }

    private fun logAll() {
        mealLocations.forEach { i("$it") }
    }
}