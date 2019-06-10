package com.example.testapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.model.GithubRepository
import com.example.testapp.model.LiveDataResult
import com.example.testapp.network.RetrofitInterface
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

class GithubViewModel(private val retrofitInterface: RetrofitInterface) : ViewModel() {

    val repositoriesLiveData = MutableLiveData<LiveDataResult<List<GithubRepository>>>()
    var loadingLiveData = MutableLiveData<Boolean>()

    fun getGithubRepositories(user: String) {
        this.setLoadingVisibility(true)
        this.retrofitInterface.getRepositories(user).subscribe(GithubConsumer())
    }

    fun setLoadingVisibility(visible: Boolean) {
        loadingLiveData.postValue(visible)
    }

    inner class GithubConsumer : MaybeObserver<List<GithubRepository>> {

        override fun onSuccess(t: List<GithubRepository>) {
            this@GithubViewModel.repositoriesLiveData.postValue(LiveDataResult.success(t))
            this@GithubViewModel.setLoadingVisibility(false)
        }

        override fun onComplete() {
            this@GithubViewModel.setLoadingVisibility(false)
        }

        override fun onSubscribe(d: Disposable) {
            this@GithubViewModel.repositoriesLiveData.postValue(LiveDataResult.loading())
        }

        override fun onError(e: Throwable) {
            this@GithubViewModel.repositoriesLiveData.postValue(LiveDataResult.error(e))
            this@GithubViewModel.setLoadingVisibility(false)
        }

    }


}