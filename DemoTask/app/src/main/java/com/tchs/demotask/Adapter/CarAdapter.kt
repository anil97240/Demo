package com.tchs.demotask.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tchs.demotask.R
import com.tchs.demotask.Response.CarResponse
import java.util.*
import kotlin.collections.ArrayList

class CarAdapter(private val context: Context, var carList: ArrayList<CarResponse.Result>) :
    RecyclerView.Adapter<CarAdapter.ImageViewVH>(), Filterable {
    private var carFilter: ArrayList<CarResponse.Result> = carList

    inner class ImageViewVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCarId = itemView.findViewById<TextView>(R.id.tv_car_id)!!
        var tvCarName = itemView.findViewById<TextView>(R.id.tv_car_name)!!
        var tvCarModel = itemView.findViewById<TextView>(R.id.tv_car_model)!!
        var tvCarColor = itemView.findViewById<TextView>(R.id.tv_car_color)!!
        var tvCarmodelyear = itemView.findViewById<TextView>(R.id.tv_car_model_year)!!
        var tvCarPrice = itemView.findViewById<TextView>(R.id.tv_car_price)!!
        var tvAvailability = itemView.findViewById<TextView>(R.id.tv_availability)!!
        var tvCarVin = itemView.findViewById<TextView>(R.id.tv_car_vin)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return ImageViewVH(view)
    }

    override fun getItemCount(): Int {
        return if (carFilter.size > 50) {
            50
        } else {
            carFilter.size
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ImageViewVH, position: Int) {
        val data = carFilter[position]
        holder.tvCarId.text = "CAR ID : ${data.id!!}"
        holder.tvCarName.text = "CAR NAME : ${data.car!!}"
        holder.tvCarModel.text = "CAR MODEL : ${data.carModel!!}"
        holder.tvCarColor.text = "CAR COLOR : ${data.carColor!!}"
        holder.tvCarmodelyear.setText("CAR MODEL YEAR : ${data.carModelYear!!.toString()}")
        holder.tvCarPrice.text = "CAR PRICE : ${data.price!!}"
        if (data.availability == "false") {
            holder.tvAvailability.text = "CAR Available : NO"
        } else {
            holder.tvAvailability.text = "CAR Available : YES"
        }
        holder.tvCarVin.setText("CAR VIN : ${data.carVin!!}")

    }

    override fun getFilter(): Filter {
        return dataFilter
    }

    private val dataFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: java.util.ArrayList<CarResponse.Result>?
            if (constraint.isEmpty()) {
                filteredList = arrayListOf()
                filteredList.addAll(carList)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                filteredList = arrayListOf()
                for (item in carList) {
                    if (item.car!!.toLowerCase(Locale.ROOT)
                            .contains(filterPattern) || item.carColor!!.toLowerCase(
                            Locale.ROOT
                        ).contains(filterPattern) || item.carModel!!.toLowerCase(
                            Locale.ROOT
                        ).contains(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            carFilter = results.values as ArrayList<CarResponse.Result>
            notifyDataSetChanged()
        }
    }

}