package com.countries.app.utils.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.countries.app.R
import com.countries.app.data.api.model.State


class StatesAdapter(private val mList: List<State>) : RecyclerView.Adapter<StatesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflates the card_view_design view to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // Binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = mList[position]
        holder.tvStateName.text = place.name
        holder.tvStateCode.text = place.state_code
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvStateName: TextView = itemView.findViewById(R.id.tv_place_name)
        val tvStateCode: TextView = itemView.findViewById(R.id.tv_place_code)
    }
}
