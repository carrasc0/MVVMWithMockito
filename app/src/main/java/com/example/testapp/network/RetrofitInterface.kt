package com.example.testapp.network

import com.example.testapp.model.GithubRepository
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {

    @GET("user/{user}/repos")
    fun getRepositories(@Path("user") githubUser: String): Maybe<List<GithubRepository>>

}