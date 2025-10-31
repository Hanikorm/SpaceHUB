package com.hanikorm.spacehub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanikorm.spacehub.databinding.ItemApodBinding
import com.hanikorm.spacehub.model.Apod

class ApodAdapter(private var items: MutableList<Apod> = mutableListOf()) :
    RecyclerView.Adapter<ApodAdapter.ApodViewHolder>() {

    inner class ApodViewHolder(val binding: ItemApodBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateData(newItems: List<Apod>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodViewHolder {
        val binding = ItemApodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApodViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            title.text = item.title
            date.text = item.date

            if (item.mediaType == "image") {
                item.imageUrl?.let { url ->
                    Glide.with(image.context)
                        .load(url)
                        .into(image)
                }
            } else if (item.mediaType == "video") {
                image.setImageResource(android.R.drawable.ic_media_play)
            } else {
                image.setImageDrawable(null)
            }
        }
    }

    override fun getItemCount() = items.size
}
