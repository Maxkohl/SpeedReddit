package com.maxkohl.speedreddit.rapidmode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.maxkohl.speedreddit.data.RedditPost
import com.maxkohl.speedreddit.databinding.FragmentRapidModeBinding
import com.maxkohl.speedreddit.home.HomeViewModel

class RapidModeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRapidModeBinding.inflate(inflater)

        homeViewModel.getRedditPostsList()
        val redditPostList: List<RedditPost>? = homeViewModel.redditPostsLists.value
        val viewPagerAdapter = redditPostList?.let { RapidModeAdapter(this, it) }

        viewPager = binding.viewPager
        viewPager.adapter = viewPagerAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Rapid Mode"

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        viewPager.unregisterOnPageChangeCallback(viewPager2PageChangeCallback)

    }
}