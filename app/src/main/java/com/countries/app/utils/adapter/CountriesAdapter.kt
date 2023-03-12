package com.countries.app.utils.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.countries.app.R
import com.countries.app.data.api.model.Country


class CountriesAdapter(private val mList: List<Country>) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    private lateinit var mListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflates the card_view_design view to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view, mListener)
    }

    // Binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = mList[position]
        holder.tvCountryName.text = place.country
        holder.tvCountryCode.text = place.iso2
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to text
    class ViewHolder(ItemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val tvCountryName: TextView = itemView.findViewById(R.id.tv_place_name)
        val tvCountryCode: TextView = itemView.findViewById(R.id.tv_place_code)
        val cvCardView: CardView = itemView.findViewById(R.id.cv_place)
        init {
            ItemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}
