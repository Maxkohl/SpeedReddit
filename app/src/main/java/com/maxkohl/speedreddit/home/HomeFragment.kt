package com.maxkohl.speedreddit.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

            if (redditPost.isVideo) {
                findNavController().navigate(R.id.action_homeFragment_to_videoPost)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_imagePost)
            }

        })

        return binding.root
    }
}