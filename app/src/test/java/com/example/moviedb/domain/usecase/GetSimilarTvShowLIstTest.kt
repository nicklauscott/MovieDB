package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeTvShowRepository
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetSimilarTvShowLIstTest {

    private lateinit var getSimilarTvShowLIst: GetSimilarTvShowLIst
    @Before
    fun setUp() {
        val shows = mutableListOf(
            mock<TvShow>().apply {
                whenever(id).thenReturn(1001)
                whenever(genre_ids).thenReturn(listOf(3, 4, 5))
            },
            mock<TvShow>().apply {
                whenever(id).thenReturn(6872)
                whenever(genre_ids).thenReturn(listOf(1, 4))
            }
        )
        val repository = FakeTvShowRepository(tvShows = shows)
        getSimilarTvShowLIst = GetSimilarTvShowLIst(repository)
    }

    @Test
    fun `getSimilarTvShowLIst should return the similar TvShow without the current show`() = runTest {
        getSimilarTvShowLIst(1001, listOf(3, 4, 5)).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data?.find { it.id == 6872 }).isNotNull()
            assertThat(result.data?.find { it.id == 1001 }).isNull()
        }
    }
}