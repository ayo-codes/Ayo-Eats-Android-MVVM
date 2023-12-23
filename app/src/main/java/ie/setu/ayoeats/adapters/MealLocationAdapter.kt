package ie.setu.ayoeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.ayoeats.databinding.CardMealLocationBinding
import ie.setu.ayoeats.models.MealLocationModel

interface MealLocationListener {
    fun onMealLocationClick(mealLocationModel: MealLocationModel)
}

class MealLocationAdapter constructor(
    private var mealLocations: ArrayList<MealLocationModel>,
    private val listener: MealLocationListener
) : RecyclerView.Adapter<MealLocationAdapter.MainHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealLocationAdapter.MainHolder {
        val binding = CardMealLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MealLocationAdapter.MainHolder, position: Int) {
        val mealLocation = mealLocations[holder.adapterPosition]
        holder.bind(mealLocation , listener)
    }

    override fun getItemCount(): Int {
        return mealLocations.size
    }

    inner class MainHolder( val binding: CardMealLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mealLocation: MealLocationModel, listener: MealLocationListener){
//            binding.mealName.text = mealLocation.mealName
//            binding.mealDescription.text = mealLocation.mealDescription
//            binding.root.tag = mealLocation.uid
            binding.mealLocation = mealLocation
            binding.root.setOnClickListener { listener.onMealLocationClick(mealLocation) }
            binding.executePendingBindings()
        }
    }

    fun removeAt(position: Int) {
        mealLocations.removeAt(position)
        notifyItemRemoved(position)
    }

}