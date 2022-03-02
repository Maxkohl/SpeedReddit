package com.maxkohl.speedreddit.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel.getRedditPostsList()

        binding.lifecycleOwner = this
        binding.viewModel = homeViewModel
        binding.postsComposelist.setContent {
            AppCompatTheme {
                HomeScreen(homeViewModel)
            }
        }

        return binding.root
    }

    private fun handleNav(redditPost: RedditPost) {
        if (!redditPost.mediaType.isNullOrEmpty()) {
            when (redditPost.mediaType) {
                "link" -> {
                    val defaultBrowser = Intent.makeMainSelectorActivity(
                        Intent.ACTION_MAIN,
                        Intent.CATEGORY_APP_BROWSER
                    )
                    defaultBrowser.data = Uri.parse(redditPost.contentUrl)
                    startActivity(defaultBrowser)
                }
                "hosted:video" -> findNavController().navigate(R.id.action_homeFragment_to_videoPost)
                "image" -> findNavController().navigate(R.id.action_homeFragment_to_imagePost)
                else -> findNavController().navigate(R.id.action_homeFragment_to_textPost)
            }
        }
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val redditPosts by homeViewModel.redditPostsLists.observeAsState()

    redditPosts?.let { RedditPostList(it) }
}

@Composable
fun RedditPostList(redditPosts: List<RedditPost>) {

    LazyColumn() {
        items(redditPosts) { redditPost ->
            RedditListItem(redditPost)
        }
    }
}

@Composable
fun RedditListItem(redditPost: RedditPost) {

    Card(Modifier.padding(vertical = 5.dp).fillMaxWidth() ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.reddit_snoo),
                contentDescription = "Yeehaw",
                modifier = Modifier.size(60.dp)
            )
            Column(Modifier.fillMaxWidth()) {
                Row() {
                    Text(text = redditPost.title)
                }
                Spacer(Modifier.height(20.dp))
                Row() {
                    Text(text = redditPost.subreddit, textAlign = TextAlign.Start)
                    Spacer(Modifier.width(20.dp))
                    Text(text = redditPost.upvoteCount, textAlign = TextAlign.End)
                }
            }
        }
    }
}

@Preview
@Composable
fun RedditPostListPreview() {
    val redditPost = listOf<RedditPost>()

    RedditPostList(redditPost)
}