package com.hanikorm.spacehub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanikorm.spacehub.databinding.ItemMarsPhotoBinding
import com.hanikorm.spacehub.model.MarsPhoto

class MarsPhotosAdapter : RecyclerView.Adapter<MarsPhotosAdapter.MarsPhotoViewHolder>() {

    private var photos: List<MarsPhoto> = emptyList()

    fun submitList(list: List<MarsPhoto>) {
        photos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPhotoViewHolder {
        val binding = ItemMarsPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarsPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarsPhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size

    class MarsPhotoViewHolder(private val binding: ItemMarsPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: MarsPhoto) {
            binding.cameraNameTextView.text = photo.camera.fullName
            binding.earthDateTextView.text = photo.earthDate

            Glide.with(itemView.context)
                .load(photo.imgSrc.replace("http://", "https://")) // API иногда возвращает http
                .placeholder(R.drawable.ic_launcher_background) // Простое временное изображение
                .error(R.drawable.ic_launcher_foreground) // Изображение для ошибки
                .into(binding.marsImageView)
        }
    }
}
