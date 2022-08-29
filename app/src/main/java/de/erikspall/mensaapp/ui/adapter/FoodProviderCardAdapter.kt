package de.erikspall.mensaapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.source.local.DummyDataSource
import de.erikspall.mensaapp.domain.utils.Extensions.getDynamicColorIfAvailable
import de.erikspall.mensaapp.ui.canteenlist.CanteenListFragmentDirections

class FoodProviderCardAdapter(
    private val context: Context?,
    private val navController: NavController
) : RecyclerView.Adapter<FoodProviderCardAdapter.FoodProviderViewHolder>() {

    private val dummyList = DummyDataSource.canteens

    class FoodProviderViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        val foodProviderImage: ImageView = view!!.findViewById(R.id.image_food_provider)
        val foodProviderTypeChip: Chip = view!!.findViewById(R.id.chip_food_provider_type)
        val foodProviderOpeningInfoText: MaterialTextView = view!!.findViewById(R.id.text_food_provider_opening_info)
        val foodProviderNameText: MaterialTextView = view!!.findViewById(R.id.text_food_provider_name)
        val imageViewTime: ImageView = view!!.findViewById(R.id.image_view_time)
        val container: MaterialCardView = view!!.findViewById(R.id.container_food_provider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodProviderViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_food_provider, parent, false)
        return FoodProviderViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n") // TODO: Change later
    override fun onBindViewHolder(holder: FoodProviderViewHolder, position: Int) {
        val item = dummyList[position]
        holder.foodProviderImage.setImageResource(item.getImageResourceId())
        holder.foodProviderNameText.text = item.getName()
        holder.foodProviderTypeChip.text = item.getType().toString()
        if (item.getOpeningHours().get(0).isOpen()) {
            holder.foodProviderOpeningInfoText.text = "Noch 2h geöffnet"
        } else {
            val drawableWrap = DrawableCompat.wrap(AppCompatResources.getDrawable(context!!, R.drawable.ic_time)!!).mutate();
            val errorColor = context.getDynamicColorIfAvailable(R.attr.colorError)
            holder.imageViewTime.setColorFilter(errorColor)

            holder.foodProviderOpeningInfoText.text = "Geschlossen!"
            holder.foodProviderOpeningInfoText.setTextColor(errorColor)

        }
        holder.container.setOnClickListener {
            val action = CanteenListFragmentDirections.actionOpenDetails(position)
            navController.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dummyList.size
    }
}