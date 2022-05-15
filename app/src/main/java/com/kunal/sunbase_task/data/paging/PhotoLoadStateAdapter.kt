package com.kunal.sunbase_task.data.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kunal.sunbase_task.databinding.FooterBinding

class PhotoLoadStateAdapter(
    private val onRetry: () -> Unit
) : LoadStateAdapter<PhotoLoadStateAdapter.PhotoLoadStateViewHolder>() {

    inner class PhotoLoadStateViewHolder(
        private val binding: FooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(loadState: LoadState) {
            binding.apply {
                loadingAnim.isVisible = loadState is LoadState.Loading
            }
        }

    }

    override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotoLoadStateViewHolder {
        val binding =
            FooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoLoadStateViewHolder(binding)
    }
}