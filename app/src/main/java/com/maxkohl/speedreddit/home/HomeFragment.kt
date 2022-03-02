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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
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
                HomeScreen(homeViewModel, onNavigate = { handleNav() })
            }
        }

        return binding.root
    }

    private fun handleNav() {
        val redditPost = homeViewModel.redditPost.value
        if (redditPost != null) {
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
fun HomeScreen(homeViewModel: HomeViewModel, onNavigate: () -> Unit) {
    val redditPosts by homeViewModel.redditPostsLists.observeAsState()

    redditPosts?.let { RedditPostList(homeViewModel, it, onNavigate) }
}

@Composable
fun RedditPostList(
    homeViewModel: HomeViewModel,
    redditPosts: List<RedditPost>,
    onNavigate: () -> Unit
) {

    LazyColumn() {
        items(redditPosts) { redditPost ->
            RedditListItem(homeViewModel, redditPost, onNavigate)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RedditListItem(
    homeViewModel: HomeViewModel,
    redditPost: RedditPost,
    onNavigate: () -> Unit
) {

    Card(
        onClick = {
            homeViewModel.onPostClicked(redditPost)
            onNavigate()
        }, Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                painter = rememberImagePainter(
                    data = redditPost.previewImgSrc,
                    builder = { error(R.drawable.reddit_snoo) }),
                contentDescription = "Reddit post image",
                modifier = Modifier
                    .size(60.dp)
                    .align(CenterVertically)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Row() {
                    Text(text = redditPost.title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(20.dp))
                Row() {
                    Text(text = redditPost.subreddit, fontSize = 14.sp)
                    Spacer(Modifier.width(60.dp))
                    Text(
                        text = redditPost.upvoteCount,
                        Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RedditPostListPreview() {
//    val redditPost = listOf<RedditPost>()
//
//    RedditPostList(redditPost)
//}