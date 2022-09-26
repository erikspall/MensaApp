package de.erikspall.mensaapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textview.MaterialTextView
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.domain.utils.Extensions.getDynamicColorIfAvailable
import java.util.stream.Collectors

//import de.erikspall.mensaapp.domain.model.enums.Role
//import de.erikspall.mensaapp.domain.model.interfaces.Menu

class MenuAdapter(
    private val context: Context,
    private val menusHolder: ConstraintLayout,
    var warningsEnabled: Boolean,
    var role: Role
): ListAdapter<Menu, MenuAdapter.MenuViewHolder>(
    MENU_COMPARATOR
)  {

    class MenuViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        val textDate: MaterialTextView = view!!.findViewById(R.id.text_menu_date)
        val layoutMenus: LinearLayout = view!!.findViewById(R.id.linear_layout_menus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n") // TODO: Change later
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        Log.d("MenuAdapter", "Binding: ${getItem(position).date}")
        holder.textDate.text = "Essen am ${getItem(position).date.dayOfWeek}," +
                " den ${getItem(position).date}"

        var index = 0
        for (meal in getItem(position).meals) {
            val mealViewHolder = LayoutInflater.from(context)
                .inflate(R.layout.item_meal, holder.layoutMenus, false)
            mealViewHolder.findViewById<MaterialTextView>(R.id.text_meal_name).text = meal.name
            mealViewHolder.findViewById<MaterialTextView>(R.id.text_meal_price).text =
                meal.getPrice(role)

            val warningIcon = mealViewHolder.findViewById<AppCompatImageView>(R.id.image_meal_error)

            mealViewHolder.findViewById<Chip>(R.id.chip_meal_category).text = meal.ingredients.stream().map {
                if (it.getUserDoesNotLike())
                    if (warningIcon.visibility == View.INVISIBLE && warningsEnabled)
                        warningIcon.visibility = View.VISIBLE
                it.getName()
            }.collect(Collectors.joining(", "))



            val layout = mealViewHolder.findViewById<ConstraintLayout>(R.id.layout_menu)

            val buttonExpand: MaterialButton =
                mealViewHolder.findViewById(R.id.button_expand_meal)

            val containerAllergenics =
                mealViewHolder.findViewById<LinearLayout>(R.id.container_allergenics)

            val chipGroupAllergenics =
                mealViewHolder.findViewById<ChipGroup>(R.id.chip_group_allergenics)

            meal.allergens.stream().forEach { mealComponent ->
                Chip(context).apply {
                    text = mealComponent.getName()
                    setEnsureMinTouchTargetSize(false)
                    isClickable = false
                    if (mealComponent.getUserDoesNotLike()) {
                        chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_info)
                        if (warningIcon.visibility == View.INVISIBLE && warningsEnabled)
                            warningIcon.visibility = View.VISIBLE
                    }
                    chipGroupAllergenics.addView(this)

                }
            }


            buttonExpand.setOnClickListener {
                val v = if (containerAllergenics.visibility == View.GONE) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(menusHolder, AutoTransition())
                containerAllergenics.visibility = v
            }

            holder.layoutMenus.addView(mealViewHolder)
            index++
        }
    }

    companion object {
        private val MENU_COMPARATOR = object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(
                oldItem: Menu,
                newItem: Menu
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: Menu,
                newItem: Menu
            ): Boolean {
                return oldItem.date == newItem.date &&
                        oldItem.meals.containsAll(newItem.meals)
            }
        }
    }
}