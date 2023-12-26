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
    var profilepic: String = "",
    var email: String = "joe@bloggs.com",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0

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
            "profilepic" to profilepic,
            "email" to email,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}