package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Category
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTvShowList @Inject constructor(
    private val tvRepository: TvShowRepository
) {
    suspend operator fun invoke(
        forceFetchFromRemote: Boolean, category: Category, page: Int): Flow<Resource<List<TvShow>>> {
        if (category == Category.MyList) return tvRepository.getShowsInMyList()
        return tvRepository.getTvShows(forceFetchFromRemote, category.value, page)
    }

}

