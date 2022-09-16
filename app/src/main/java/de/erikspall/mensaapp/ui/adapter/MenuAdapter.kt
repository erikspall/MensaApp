package de.erikspall.mensaapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textview.MaterialTextView
import de.erikspall.mensaapp.R
//import de.erikspall.mensaapp.domain.model.enums.Role
//import de.erikspall.mensaapp.domain.model.interfaces.Menu

class MenuAdapter(
    private val context: Context,
 //   private val data: List<Menu>,
    private val menusHolder: ConstraintLayout
): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>()  {


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
        /*Log.d("MenuAdapter", "Binding: ${data[position].getDate()}")
        holder.textDate.text = "Essen am ${data[position].getDate().dayOfWeek}," +
                " den ${data[position].getDate()}"

        var index = 0
        for (meal in data[position].getMeals()) {
            val mealViewHolder = LayoutInflater.from(context)
                .inflate(R.layout.item_meal, holder.layoutMenus, false)
            mealViewHolder.findViewById<MaterialTextView>(R.id.text_meal_name).text = meal.getName()
            mealViewHolder.findViewById<MaterialTextView>(R.id.text_meal_price).text =
                meal.getPrice(Role.STUDENT).toString()
            mealViewHolder.findViewById<Chip>(R.id.chip_meal_category).text = meal.getCategory()

            val layout = mealViewHolder.findViewById<ConstraintLayout>(R.id.layout_menu)

            val buttonExpand: MaterialButton =
                mealViewHolder.findViewById(R.id.button_expand_meal)

            val containerAllergenics =
                mealViewHolder.findViewById<LinearLayout>(R.id.container_allergenics)

            val chipGroupAllergenics =
                mealViewHolder.findViewById<ChipGroup>(R.id.chip_group_allergenics)

            val test = Chip(context)
            test.text = "Hi"
            chipGroupAllergenics.addView(test)

            buttonExpand.setOnClickListener {
                val v = if (containerAllergenics.visibility == View.GONE) View.VISIBLE else View.GONE
                TransitionManager.beginDelayedTransition(menusHolder, AutoTransition())
                containerAllergenics.visibility = v
            }

            holder.layoutMenus.addView(mealViewHolder)
            index++
        }*/
    }

    override fun getItemCount(): Int {
        return 0
    }
}