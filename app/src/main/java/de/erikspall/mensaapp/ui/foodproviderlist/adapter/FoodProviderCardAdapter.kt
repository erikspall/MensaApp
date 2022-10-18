package de.erikspall.mensaapp.ui.foodproviderlist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.erikspall.mensaapp.databinding.ItemFoodProviderBinding
import de.erikspall.mensaapp.domain.model.FoodProvider

class FoodProviderCardAdapter(
) : ListAdapter<FoodProvider, FoodProviderCardAdapter.FoodProviderViewHolder>(
    FOOD_PROVIDER_COMPARATOR
) {

    inner class FoodProviderViewHolder(
        val binding: ItemFoodProviderBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            foodProvider: FoodProvider
        ) {
            // To get strings etc.
            val resources = binding.root.resources



            binding.imageFoodProvider.setImageResource(foodProvider.photo)

            //Glide.with(binding.imageFoodProvider.context)
             //   .load(foodProvider.photo)
             //   .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
             //   .into(binding.imageFoodProvider)

            binding.textFoodProviderName.text = foodProvider.name
            binding.textFoodProviderOpeningInfo.text = foodProvider.openingHoursString
            binding.chipFoodProviderType.text = foodProvider.type

            binding.root.setOnClickListener {
                Log.d("$TAG:onClickListener", "To be implemented")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodProviderViewHolder {
        return FoodProviderViewHolder(ItemFoodProviderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FoodProviderViewHolder, position: Int) {
        val foodProvider = getItem(position)
        holder.bind(foodProvider)
    }


    companion object{
        const val TAG = "FoodProviderCardAdapter"

        private val FOOD_PROVIDER_COMPARATOR = object : DiffUtil.ItemCallback<FoodProvider>() {
            override fun areItemsTheSame(oldItem: FoodProvider, newItem: FoodProvider): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: FoodProvider, newItem: FoodProvider): Boolean {
                return oldItem == newItem
            }
        }
    }
}