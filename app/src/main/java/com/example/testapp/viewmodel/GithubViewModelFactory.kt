package com.example.testapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.network.RetrofitInterface
import java.lang.IllegalArgumentException

class GithubViewModelFactory(private val retrofitInterface: RetrofitInterface) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubViewModel::class.java)) {
            return GithubViewModel(this.retrofitInterface) as T
        }
        throw IllegalArgumentException("Unknow View Model class")
    }

}