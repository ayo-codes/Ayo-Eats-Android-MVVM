package ie.setu.ayoeats.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize


@Parcelize
data class MealLocationModel(
    var uid: String? = "",
    var mealName: String = "",
    var mealDescription: String = "",
    var mealPrice: Double = 0.00,
    var mealRating: Double = 0.00,
    var email: String = "joe@bloggs.com"
) :
    Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "mealName" to mealName,
            "mealDescription" to mealDescription,
            "mealPrice" to mealPrice,
            "mealRating" to mealRating,
            "email" to email
        )
    }
}