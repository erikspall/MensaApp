package de.erikspall.mensaapp.ui.foodproviderlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.erikspall.mensaapp.databinding.ItemFoodProviderBinding
import de.erikspall.mensaapp.domain.model.FoodProvider

/**
 * RecyclerView adapter for a list of Restaurants.
 */
open class FoodProviderCardAdapter(query: Query, private val listener: OnFoodProviderSelectedListener) :
    FirestoreAdapter<FoodProviderCardAdapter.FoodProviderViewHolder>(query) {

    interface OnFoodProviderSelectedListener {

        fun onFoodProviderSelected(foodProvider: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodProviderViewHolder {
        return FoodProviderViewHolder(ItemFoodProviderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FoodProviderViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class FoodProviderViewHolder(val binding: ItemFoodProviderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnFoodProviderSelectedListener?
        ) {
            val foodProvider = snapshot.toObject<FoodProvider>() ?: return

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
                listener?.onFoodProviderSelected(snapshot)
            }
        }
    }
}