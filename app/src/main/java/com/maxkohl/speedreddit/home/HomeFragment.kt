package com.maxkohl.speedreddit.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
        binding.postsRecyclerview.adapter = HomeListAdapter(RedditPostListener { redditPost ->
            homeViewModel.onPostClicked(redditPost)
            handleNav(redditPost)
        })

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