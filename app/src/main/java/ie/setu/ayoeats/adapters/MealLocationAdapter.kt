package ie.setu.ayoeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.ayoeats.databinding.CardMealLocationBinding
import ie.setu.ayoeats.models.MealLocationModel
import ie.setu.ayoeats.utils.customTransformation

interface MealLocationListener {
    fun onMealLocationClick(mealLocationModel: MealLocationModel)
}

class MealLocationAdapter constructor(
    private var mealLocations: ArrayList<MealLocationModel>,
    private val listener: MealLocationListener,
    private val readOnly: Boolean
) : RecyclerView.Adapter<MealLocationAdapter.MainHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealLocationAdapter.MainHolder {
        val binding = CardMealLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MealLocationAdapter.MainHolder, position: Int) {
        val mealLocation = mealLocations[holder.adapterPosition]
        holder.bind(mealLocation , listener)
    }

    override fun getItemCount(): Int {
        return mealLocations.size
    }

    inner class MainHolder( val binding: CardMealLocationBinding , private  val readOnly: Boolean) : RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly
        fun bind(mealLocation: MealLocationModel, listener: MealLocationListener){
//            binding.mealName.text = mealLocation.mealName
//            binding.mealDescription.text = mealLocation.mealDescription
            binding.root.tag = mealLocation
            binding.mealLocation = mealLocation
            Picasso.get().load(mealLocation.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onMealLocationClick(mealLocation) }
            binding.executePendingBindings()
        }
    }

    fun removeAt(position: Int) {
        mealLocations.removeAt(position)
        notifyItemRemoved(position)
    }

}