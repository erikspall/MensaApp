package de.erikspall.mensaapp.ui.foodproviderdetail.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.textview.MaterialTextView
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.model.Menu
import de.erikspall.mensaapp.domain.utils.Extensions.getDynamicColorIfAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuAdapter2(
    private val menus: List<Menu>,
    var warningsEnabled: Boolean,
    var role: Role,
    private val lifecycleScope: CoroutineScope,
    val onFinishedConstructing: () -> Unit
) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return menus.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.item_menu, container, false)

       //val textDayOfWeek: MaterialTextView = view!!.findViewById(R.id.text_day_of_week)
        //val textDate: MaterialTextView = view!!.findViewById(R.id.text_menu_date)
        val layoutMenus: LinearLayout = view!!.findViewById(R.id.linear_layout_menus)
        //val divider: MaterialDivider = view!!.findViewById(R.id.divider)

        lifecycleScope.launch {
            populateMenuLayout(layoutMenus, position)
        }

        container.addView(view)
        return view
    }

    private suspend fun populateMenuLayout(layoutMenu: LinearLayout, position: Int) {
        val context: Context = layoutMenu.context

        for (meal in menus[position].meals) {
            val mealViewHolder = LayoutInflater.from(context)
                .inflate(R.layout.item_meal, layoutMenu, false)
            lateinit var textMealName: MaterialTextView
            lateinit var textMealPrice: MaterialTextView
            lateinit var warningIcon: AppCompatImageView
            lateinit var chipMealCategory: Chip
            lateinit var buttonExpand: MaterialButton
            lateinit var containerAllergenic: LinearLayout
            lateinit var chipGroupAllergenic: ChipGroup

            withContext(Dispatchers.IO) {
                textMealName = mealViewHolder.findViewById(R.id.text_meal_name)
                textMealPrice = mealViewHolder.findViewById(R.id.text_meal_price)

                warningIcon = mealViewHolder.findViewById(R.id.image_meal_error)

                chipMealCategory = mealViewHolder.findViewById(R.id.chip_meal_category)

                //layout = mealViewHolder.findViewById(R.id.layout_menu)

                buttonExpand = mealViewHolder.findViewById(R.id.button_expand_meal)

                containerAllergenic = mealViewHolder.findViewById(R.id.container_allergenics)

                chipGroupAllergenic = mealViewHolder.findViewById(R.id.chip_group_allergenics)
            }

            textMealName.text = meal.name
            textMealPrice.text = meal.prices[role]

            chipMealCategory.text = meal.additives.filter { it.type == AdditiveType.INGREDIENT }
                .joinToString { ingredient ->
                    if (ingredient.isNotLiked && warningsEnabled) {
                        warningIcon.visibility = View.VISIBLE
                    }

                    /* return */ ingredient.name
                }

            // TODO: Hide mealCategory if none set

            meal.additives.filter { it.type == AdditiveType.ALLERGEN }.forEach { allergen ->
                Chip(context).apply {
                    text = allergen.name
                    setEnsureMinTouchTargetSize(false)
                    isClickable = false
                    if (allergen.isNotLiked && warningsEnabled) {
                        chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_info)
                        val colorError = context.getDynamicColorIfAvailable(R.attr.colorError)
                        chipIconTint = ColorStateList.valueOf(colorError)
                        setTextColor(colorError)
                        warningIcon.visibility = View.VISIBLE
                    }
                    chipGroupAllergenic.addView(this)
                }
            }

            buttonExpand.setOnClickListener { button ->
                val v =
                    if (containerAllergenic.visibility == View.GONE) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(layoutMenu, AutoTransition())
                containerAllergenic.visibility = v

                if (v == View.VISIBLE) {
                    button.animate().rotationBy(-180f).apply {
                        duration = 100
                        interpolator = AccelerateInterpolator()
                        withEndAction {
                            button.rotation = 180f
                        }
                    }
                } else {
                    button.animate().rotationBy(180f).apply {
                        duration = 100
                        interpolator = AccelerateInterpolator()
                        withEndAction {
                            button.rotation = 0f
                        }
                    }
                }
            }

            layoutMenu.addView(mealViewHolder)
        }
        if ((count - 1) == position)
            onFinishedConstructing()
    }
}