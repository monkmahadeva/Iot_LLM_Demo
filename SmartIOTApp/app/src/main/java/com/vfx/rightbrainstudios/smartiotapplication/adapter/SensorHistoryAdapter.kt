package com.vfx.rightbrainstudios.smartiotapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.smartiotapplication.R

class SensorHistoryAdapter : RecyclerView.Adapter<SensorHistoryAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<SensorData>()

    fun submitList(newList: List<SensorData>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: SensorData) {
            itemView.findViewById<TextView>(R.id.textTemperature).text = "Temperature: ${data.temperature}°C"
            itemView.findViewById<TextView>(R.id.textHumidity).text = "Humidity: ${data.humidity}%"
            itemView.findViewById<TextView>(R.id.textTimestamp).text = data.timestamp
        }
    }
}