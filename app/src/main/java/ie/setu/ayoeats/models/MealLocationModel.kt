package ie.setu.ayoeats.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MealLocationModel(
    var uid : Long = 0L,
    var mealName: String = "",
    var mealDescription: String = "",
    var mealPrice: Double = 0.00,
    var mealRating: Double = 0.00
) :
    Parcelable
