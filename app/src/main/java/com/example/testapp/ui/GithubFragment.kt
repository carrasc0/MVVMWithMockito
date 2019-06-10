package com.example.testapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.testapp.R
import com.example.testapp.model.GithubRepository
import com.example.testapp.model.LiveDataResult
import com.example.testapp.network.AppApplication
import com.example.testapp.network.RetrofitInterface
import com.example.testapp.viewmodel.GithubViewModel
import com.example.testapp.viewmodel.GithubViewModelFactory

class GithubFragment : Fragment() {

    private lateinit var viewModel: GithubViewModel

    companion object {
        fun newInstance() = GithubFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_github, container, false)
    }

    private val liveDataObserver = Observer<LiveDataResult<List<GithubRepository>>> { result ->
        when (result.status) {
            LiveDataResult.Status.LOADING -> {
                //loading data
            }
            LiveDataResult.Status.SUCCESS -> {
                //get data
            }
            LiveDataResult.Status.ERROR -> {
                //manage error
            }

        }
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        //show hide progress
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory =
            GithubViewModelFactory((activity?.application as AppApplication).retrofit.create(RetrofitInterface::class.java))
        this.viewModel = ViewModelProviders.of(this, factory).get(GithubViewModel::class.java)
        this.viewModel.repositoriesLiveData.observe(this, this.liveDataObserver)
        this.viewModel.loadingLiveData.observe(this, this.loadingObserver)
    }
}


