package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MenuAdapter(private val context: Context, private val menuItems: List<MenuReceiver.MenuItem>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return menuItems.size
    }

    override fun getItem(position: Int): Any {
        return menuItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_menu, parent, false)
            holder = ViewHolder()
            holder.menuItemNameTextView = view.findViewById(R.id.food_name)
            holder.menuItemDescriptionTextView = view.findViewById(R.id.food_description)
            holder.menuItemPriceTextView = view.findViewById(R.id.food_price)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val menuItem = getItem(position) as MenuReceiver.MenuItem
        holder.menuItemNameTextView.text = menuItem.foodname
        holder.menuItemDescriptionTextView.text = menuItem.description
        holder.menuItemPriceTextView.text = menuItem.price.toString()

        return view!!
    }

    private class ViewHolder {
        lateinit var menuItemNameTextView: TextView
        lateinit var menuItemDescriptionTextView: TextView
        lateinit var menuItemPriceTextView: TextView
    }
}
