package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeTvShowRepository
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Category
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTvShowListTest {

    private lateinit var getTvShowList: GetTvShowList

    @Before
    fun setup() {
        val shows = mutableListOf(
            mock<TvShow>().apply {
                whenever(id).thenReturn(1001)
                whenever(inMyList).thenReturn(false)
            },
            mock<TvShow>().apply {
                whenever(id).thenReturn(6872)
                whenever(inMyList).thenReturn(true)
            },
            mock<TvShow>().apply {
                whenever(id).thenReturn(2345)
                whenever(inMyList).thenReturn(true)
            },
            mock<TvShow>().apply {
                whenever(id).thenReturn(1234)
                whenever(inMyList).thenReturn(false)
            }
        )
        val repository = FakeTvShowRepository(tvShows = shows)
        getTvShowList = GetTvShowList(repository)
    }

    @Test
    fun `getTvShowList should return list of TvShows`() = runTest {
        getTvShowList(false, Category.Popular, -1) { result ->
            assertThat(result?.size).isEqualTo(4)
        }
    }

    @Test
    fun `getTvShowList should return list of TvShows in my list if specified in category`() = runTest {
        getTvShowList(false, Category.MyList, -1) { result ->
            assertThat(result?.size).isEqualTo(2)
            assertThat(result?.find { it.id == 1001 }).isNull()
            assertThat(result?.find { it.id == 2345 }).isNotNull()
        }
    }

}