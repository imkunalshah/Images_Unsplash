package com.kunal.sunbase_task.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.data.repositories.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageViewModel
@Inject
constructor(
    private val imageRepository: ImageRepository,
) : ViewModel() {

    var isConnected = false

    fun getPhotos(): LiveData<PagingData<Photo>>? {
        var result: LiveData<PagingData<Photo>>? = null
        viewModelScope.launch {
            val response = imageRepository.getPhotos().cachedIn(viewModelScope)
            result = response
        }
        return result
    }

    private var searchJob: Job? = null

    fun searchPhotos(query: String): LiveData<PagingData<Photo>>? {
        searchJob?.cancel()
        var result: LiveData<PagingData<Photo>>? = null
        searchJob = viewModelScope.launch {
            val response = imageRepository.searchPhotos(query).cachedIn(viewModelScope)
            Timber.d(response.value.toString())
            result = response
        }
        return result
    }

}