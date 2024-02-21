package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Category
import javax.inject.Inject

class GetTvShowList @Inject constructor(
    private val tvRepository: TvShowRepository
) {
    suspend operator fun invoke(
        forceFetchFromRemote: Boolean, category: Category, page: Int,
        tvShows: (List<TvShow>?) -> Unit
    ) {
        when (category) {
            Category.MyList -> {
                tvRepository.getShowsInMyList().collect {
                    tvShows(it.data)
                }
            }
            else -> {
                tvRepository.getTvShows(forceFetchFromRemote, category.value, page).collect {
                    tvShows(it.data)
                }
            }
        }
    }

}

