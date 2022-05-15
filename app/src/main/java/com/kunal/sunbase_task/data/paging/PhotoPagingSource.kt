package com.kunal.sunbase_task.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kunal.sunbase_task.data.network.apis.ImagesApi
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.utils.NoInternetException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class PhotoPagingSource(
    private val imagesApi: ImagesApi,
    private val query: String? = null
) : PagingSource<Int, Photo>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            when (query) {
                null -> {
                    val response = imagesApi.getPhotos(
                        page = page,
                        perPage = params.loadSize
                    ).body() ?: emptyList()
                    LoadResult.Page(
                        data = response,
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = if (response.isEmpty()) null else page + 1
                    )
                }
                else -> {
                    val response = imagesApi.searchPhotos(
                        query,
                        page,
                        params.loadSize
                    ).body()?.results ?: emptyList()
                    LoadResult.Page(
                        data = response,
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = if (response.isEmpty()) null else page + 1
                    )
                }
            }
        } catch (exception: IOException) {
            Timber.e(exception)
            LoadResult.Error(exception)
        } catch (httpException: HttpException) {
            Timber.e(httpException)
            LoadResult.Error(httpException)
        } catch (noInternetException: NoInternetException) {
            Timber.e(noInternetException)
            LoadResult.Error(noInternetException)
        }
    }

}