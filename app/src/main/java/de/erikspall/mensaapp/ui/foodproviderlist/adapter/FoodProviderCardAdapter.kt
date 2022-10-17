package de.erikspall.mensaapp.ui.foodproviderlist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.erikspall.mensaapp.databinding.ItemFoodProviderBinding
import de.erikspall.mensaapp.domain.model.FoodProvider

class FoodProviderCardAdapter(
) : ListAdapter<FoodProvider, FoodProviderCardAdapter.FoodProviderViewHolder>(
    FOOD_PROVIDER_COMPARATOR
) {

    inner class FoodProviderViewHolder(
        private val binding: ItemFoodProviderBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            foodProvider: FoodProvider
        ) {
            // To get strings etc.
            val resources = binding.root.resources

            Glide.with(binding.imageFoodProvider.context)
                .load(foodProvider.photo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imageFoodProvider)

            binding.textFoodProviderName.text = foodProvider.name
            binding.textFoodProviderOpeningInfo.text = "To be implemented"
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