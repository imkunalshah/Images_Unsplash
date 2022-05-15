package com.kunal.sunbase_task.ui.fragments

import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.data.paging.PhotoLoadStateAdapter
import com.kunal.sunbase_task.databinding.FragmentHomeBinding
import com.kunal.sunbase_task.ui.adapters.PhotoListAdapter
import com.kunal.sunbase_task.ui.base.BaseFragment
import com.kunal.sunbase_task.ui.viewmodels.ImageViewModel
import com.kunal.sunbase_task.utils.NoInternetException
import com.kunal.sunbase_task.utils.gone
import com.kunal.sunbase_task.utils.showNetworkUnavailableSnackBar
import com.kunal.sunbase_task.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: ImageViewModel by activityViewModels()

    companion object {
        const val TAG = "homeFragment"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val _imageListAdapter by lazy {
        PhotoListAdapter(
            ::onPhotoClick
        )
    }

    private var imageListAdapter: PhotoListAdapter? = null
        get() {
            kotlin.runCatching {
                field = _imageListAdapter
            }.onFailure {
                Timber.d("Error: $it")
                field = null
            }
            return field
        }

    private fun onPhotoClick(photo: Photo?) {
        val fullScreenDialogFragment = FullScreenDialogFragment.newInstance(photo)
        fullScreenDialogFragment.show(childFragmentManager, FullScreenDialogFragment.TAG)
    }

    override fun initializeViews() {
        binding.apply {
            val layoutManager = GridLayoutManager(context, 2)
            imageList.setHasFixedSize(true)
            imageList.layoutManager = layoutManager
            imageList.adapter = imageListAdapter?.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter {
                    imageListAdapter?.retry()
                },
                footer = PhotoLoadStateAdapter {
                    imageListAdapter?.retry()
                }
            )
            imageListAdapter?.addLoadStateListener { loadState ->
                val error = when {
                    loadState.source.prepend is LoadState.Error -> loadState.source.prepend as LoadState.Error
                    loadState.source.append is LoadState.Error -> loadState.source.append as LoadState.Error
                    loadState.source.refresh is LoadState.Error -> loadState.source.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    if (it.error.cause is NoInternetException) {
                        binding.root.showNetworkUnavailableSnackBar {}
                    }
                }
            }
            scrollToTop.setOnClickListener {
                imageList.smoothScrollToPosition(0)
            }
            imageList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstCompletelyVisibleItemPosition =
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstCompletelyVisibleItemPosition >= 6) {
                        showScrollToTop()
                    } else {
                        hideScrollToTop()
                    }
                }
            })
        }
    }

    override fun initializeObservers() {
        binding.loadingAnim.visible()
        viewModel.getPhotos()?.observe(viewLifecycleOwner) {
            imageListAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
            binding.loadingAnim.gone()
        }
    }

    private fun hideScrollToTop() {
        binding.scrollToTop.gone()
    }

    private fun showScrollToTop() {
        binding.scrollToTop.visible()
    }

}