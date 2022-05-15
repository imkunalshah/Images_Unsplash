package com.kunal.sunbase_task.ui.fragments

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.databinding.FragmentSearchBinding
import com.kunal.sunbase_task.ui.adapters.PhotoListAdapter
import com.kunal.sunbase_task.ui.base.BaseFragment
import com.kunal.sunbase_task.ui.viewmodels.ImageViewModel
import com.kunal.sunbase_task.data.paging.PhotoLoadStateAdapter
import com.kunal.sunbase_task.utils.NoInternetException
import com.kunal.sunbase_task.utils.gone
import com.kunal.sunbase_task.utils.showNetworkUnavailableSnackBar
import com.kunal.sunbase_task.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: ImageViewModel by activityViewModels()

    companion object {
        const val TAG = "searchFragment"
        fun newInstance(): SearchFragment {
            return SearchFragment()
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
            searchResultsList.setHasFixedSize(true)
            searchResultsList.layoutManager = layoutManager
            searchResultsList.adapter = imageListAdapter?.withLoadStateHeaderAndFooter(
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
                searchResultsList.smoothScrollToPosition(0)
            }
            searchResultsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        initializeSearch()
        binding.clearText.setOnClickListener {
            binding.searchET.text.clear()
            binding.clearText.gone()
        }
    }

    override fun initializeObservers() {}

    private fun initializeSearch() {
        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length >= 3) {
                    performSearch(s.toString())
                    binding.clearText.visible()
                    return
                }
                if (s.isEmpty() || s.isBlank() || s.length < 3) {
                    binding.clearText.gone()
                    imageListAdapter?.submitData(lifecycle, PagingData.empty())
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
    }

    private fun hideScrollToTop() {
        binding.scrollToTop.gone()
    }

    private fun showScrollToTop() {
        binding.scrollToTop.visible()
    }

    fun performSearch(query: String) {
        if (!viewModel.isConnected) {
            binding.root.showNetworkUnavailableSnackBar { }
            return
        }
        binding.loadingAnim.visible()
        viewModel.searchPhotos(query)?.observe(viewLifecycleOwner) {
            imageListAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
            binding.loadingAnim.gone()
        }
    }
}