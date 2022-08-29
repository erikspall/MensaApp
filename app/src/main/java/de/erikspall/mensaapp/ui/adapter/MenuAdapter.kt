package de.erikspall.mensaapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.enums.Role
import de.erikspall.mensaapp.domain.model.interfaces.Menu

class MenuAdapter(
    private val context: Context,
    private val data: List<Menu>
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
        Log.d("MenuAdapter", "Binding: ${data[position].getDate()}")
        holder.textDate.text = "Essen am ${data[position].getDate().dayOfWeek}, den ${data[position].getDate()}"
        for (meal in data[position].getMeals()) {
            val mealViewHolder = LayoutInflater.from(context).inflate(R.layout.item_meal, holder.layoutMenus, false)
            mealViewHolder.findViewById<MaterialTextView>(R.id.text_meal_name).text = meal.getName()
            mealViewHolder.findViewById<MaterialTextView>(R.id.text_meal_price).text = meal.getPrice(Role.STUDENT).toString()
            mealViewHolder.findViewById<Chip>(R.id.chip_meal_category).text = meal.getCategory()
            holder.layoutMenus.addView(mealViewHolder)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}