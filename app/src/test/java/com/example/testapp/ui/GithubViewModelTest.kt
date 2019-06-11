package com.example.testapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.testapp.model.GithubRepository
import com.example.testapp.model.LiveDataResult
import com.example.testapp.network.RetrofitInterface
import com.example.testapp.viewmodel.GithubViewModel
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.net.SocketException


@RunWith(JUnit4::class)
class GithubViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var retrofitInterface: RetrofitInterface

    lateinit var githubViewModel: GithubViewModel

    //this will be run before all test
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.githubViewModel = GithubViewModel(this.retrofitInterface)
    }

    @Test
    fun getGithubRepositories_positiveResponse() {
        Mockito.`when`(this.retrofitInterface.getRepositories(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Maybe.just(ArgumentMatchers.anyList<GithubRepository>())
        }
        val observer = mock(Observer::class.java) as Observer<LiveDataResult<List<GithubRepository>>>
        this.githubViewModel.repositoriesLiveData.observeForever(observer)

        this.githubViewModel.getGithubRepositories(ArgumentMatchers.anyString())

        assertNotNull(this.githubViewModel.repositoriesLiveData.value)
        assertEquals(LiveDataResult.Status.SUCCESS, this.githubViewModel.repositoriesLiveData.value?.status)

    }

    @Test
    fun getGithubRespositories_errorResponse() {
        Mockito.`when`(this.retrofitInterface.getRepositories(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("Network problem"))
        }
        val observer = mock(Observer::class.java) as Observer<LiveDataResult<List<GithubRepository>>>
        this.githubViewModel.repositoriesLiveData.observeForever(observer)

        this.githubViewModel.getGithubRepositories(ArgumentMatchers.anyString())

        assertNotNull(this.githubViewModel.repositoriesLiveData.value)
        assertEquals(LiveDataResult.Status.ERROR, this.githubViewModel.repositoriesLiveData.value?.status)
        assert(this.githubViewModel.repositoriesLiveData.value?.err is Throwable)

    }

    @Test
    fun setLoadingVisibility_onSuccess() {
        Mockito.`when`(this.retrofitInterface.getRepositories(com.nhaarman.mockitokotlin2.any())).thenAnswer {
            return@thenAnswer Maybe.just(listOf<GithubRepository>())
        }
        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.githubViewModel)
        spiedViewModel.getGithubRepositories(ArgumentMatchers.anyString())
        verify(spiedViewModel, times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun setLoadingVisibility_onError() {
        Mockito.`when`(this.retrofitInterface.getRepositories(com.nhaarman.mockitokotlin2.any())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException())
        }
        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.githubViewModel)
        spiedViewModel.getGithubRepositories(ArgumentMatchers.anyString())
        verify(spiedViewModel, times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun setLoadingVisibility_NoData() {
        Mockito.`when`(this.retrofitInterface.getRepositories(com.nhaarman.mockitokotlin2.any()))
            .thenReturn(Maybe.create {
                it.onComplete()
            })
        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.githubViewModel)
        spiedViewModel.getGithubRepositories(ArgumentMatchers.anyString())
        verify(spiedViewModel, times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())

    }

}