package com.hanikorm.spacehub

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanikorm.spacehub.databinding.ItemApodBinding
import com.hanikorm.spacehub.model.Apod

class ApodAdapter : RecyclerView.Adapter<ApodAdapter.ApodViewHolder>() {

    private var apodList: List<Apod> = emptyList()

    var onItemClick: ((Apod) -> Unit)? = null

    fun submitList(list: List<Apod>) {
        // ИСПРАВЛЕНО: Безопасная сортировка, даже если дата null
        apodList = list.sortedByDescending { it.date ?: "" }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApodViewHolder {
        val binding = ItemApodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApodViewHolder, position: Int) {
        val apod = apodList[position]
        holder.bind(apod)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(apod)
        }
    }

    override fun getItemCount(): Int = apodList.size

    class ApodViewHolder(private val binding: ItemApodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(apod: Apod) {
            // ИСПРАВЛЕНО: Безопасное присваивание текста. Если придет null, будет пустая строка.
            binding.titleTextView.text = apod.title ?: "Без заголовка"
            binding.dateTextView.text = apod.date ?: ""
            binding.explanationTextView.text = apod.explanation ?: ""

            when (apod.mediaType) {
                "image" -> {
                    binding.videoNotSupportedTextView.visibility = View.GONE
                    binding.imageView.visibility = View.VISIBLE

                    val imageUrl = apod.url
                    if (imageUrl?.startsWith("http", ignoreCase = true) == true) {
                        Glide.with(itemView.context).load(imageUrl).into(binding.imageView)
                    } else if (imageUrl != null) {
                        val resourceId = itemView.context.resources.getIdentifier(imageUrl, "drawable", itemView.context.packageName)
                        Glide.with(itemView.context).load(resourceId).into(binding.imageView)
                    }
                }
                "video" -> {
                    binding.imageView.visibility = View.VISIBLE 
                    binding.imageView.setImageResource(android.R.color.black)
                    binding.videoNotSupportedTextView.visibility = View.VISIBLE
                    binding.videoNotSupportedTextView.text = "▶️ Видео: нажмите для просмотра"
                }
                else -> {
                    binding.imageView.visibility = View.GONE
                    binding.videoNotSupportedTextView.visibility = View.GONE
                }
            }
        }
    }
}
