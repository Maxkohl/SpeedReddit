package com.maxkohl.speedreddit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkohl.speedreddit.data.RedditApi
import com.maxkohl.speedreddit.data.RedditPost
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var _redditPostsList = MutableLiveData<List<RedditPost>>()
    val redditPostsLists: LiveData<List<RedditPost>> = _redditPostsList

    private var _redditPost = MutableLiveData<RedditPost>()
    val redditPost: LiveData<RedditPost> = _redditPost

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    fun getRedditPostsList() {
        var postsList: MutableList<RedditPost> = mutableListOf()
        viewModelScope.launch {
            try {
                val response = RedditApi.retrofitService.getAllResponses()
                response.data.children.forEach { received -> postsList.add(received.data) }
                _redditPostsList.value = postsList
                _response.value = "${response.data.children[0].data} RECEIVED"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    fun onPostClicked(redditPost: RedditPost) {
        _redditPost.value = redditPost
    }
}