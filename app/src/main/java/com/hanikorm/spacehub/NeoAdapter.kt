package com.hanikorm.spacehub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanikorm.spacehub.databinding.ItemNeoBinding
import com.hanikorm.spacehub.model.NeoObject
import java.text.DecimalFormat

class NeoAdapter(private val onNeoClicked: (NeoObject) -> Unit) : RecyclerView.Adapter<NeoAdapter.NeoViewHolder>() {

    private var neoList: List<NeoObject> = emptyList()

    fun submitList(list: List<NeoObject>) {
        neoList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeoViewHolder {
        val binding = ItemNeoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NeoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NeoViewHolder, position: Int) {
        val neo = neoList[position]
        holder.bind(neo)
        holder.itemView.setOnClickListener { onNeoClicked(neo) }
    }

    override fun getItemCount(): Int = neoList.size

    class NeoViewHolder(private val binding: ItemNeoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(neo: NeoObject) {
            binding.neoNameTextView.text = neo.name

            if (neo.isPotentiallyHazardous) {
                binding.neoHazardTextView.text = "Потенциально опасен"
                binding.neoHazardTextView.visibility = View.VISIBLE
            } else {
                binding.neoHazardTextView.visibility = View.GONE
            }

            val approach = neo.closeApproachData.firstOrNull()
            if(approach != null) {
                binding.neoApproachTextView.text = "Дата сближения: ${approach.closeApproachDate}"
                val distanceKm = approach.missDistance["kilometers"]?.toDoubleOrNull() ?: 0.0
                val formatter = DecimalFormat("#,###.##")
                binding.neoDistanceTextView.text = "Расстояние: ${formatter.format(distanceKm)} км"
            } else {
                binding.neoApproachTextView.text = ""
                binding.neoDistanceTextView.text = ""
            }
        }
    }
}
