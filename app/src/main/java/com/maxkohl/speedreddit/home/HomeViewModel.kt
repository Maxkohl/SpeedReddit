package com.maxkohl.speedreddit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkohl.speedreddit.data.MainRepository
import com.maxkohl.speedreddit.data.RedditPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var mainRepository: MainRepository
) : ViewModel() {

    private var _redditPostsList = MutableLiveData<List<RedditPost>>()
    val redditPostsLists: LiveData<List<RedditPost>> = _redditPostsList

    private var _redditPost = MutableLiveData<RedditPost>()
    val redditPost: LiveData<RedditPost> = _redditPost

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    fun getRedditPostsList() {
        val postsList: MutableList<RedditPost> = mutableListOf()
        viewModelScope.launch {
            try {
                val response = mainRepository.getRedditPostsData()
                response.data.children.forEach { received -> postsList.add(received.data) }
                _redditPostsList.value = postsList
                _response.value = "${response.data.children[0].data} RECEIVED"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }
}
