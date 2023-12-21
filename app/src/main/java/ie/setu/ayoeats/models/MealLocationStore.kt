package ie.setu.ayoeats.models

interface MealLocationStore {
    fun create (mealLocation : MealLocationModel)
    fun findAll() : ArrayList<MealLocationModel>
}