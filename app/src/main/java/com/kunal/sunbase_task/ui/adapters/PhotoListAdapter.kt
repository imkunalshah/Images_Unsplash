package com.kunal.sunbase_task.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.databinding.LayoutImageBinding
import com.kunal.sunbase_task.utils.loadImage

class PhotoListAdapter(
    private val clicked: (photo: Photo?) -> Unit
) : PagingDataAdapter<Photo, PhotoListAdapter.PhotoListViewHolder>(
    PhotoDiffCallback()
) {

    inner class PhotoListViewHolder(
        private val binding: LayoutImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo?) {
            binding.imageView.loadImage(
                photo?.urls?.regular,
                binding.imageView
            )
            binding.root.setOnClickListener {
                clicked.invoke(photo)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val binding =
            LayoutImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    private class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }


}