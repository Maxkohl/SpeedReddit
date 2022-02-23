package com.maxkohl.speedreddit.postmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.maxkohl.speedreddit.R
import com.maxkohl.speedreddit.databinding.FragmentHomeBinding
import com.maxkohl.speedreddit.databinding.FragmentTextPostBinding
import com.maxkohl.speedreddit.home.HomeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TextPost.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextPost : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTextPostBinding.inflate(inflater)
        viewModel.getRedditPostsList()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.redditPost.value?.title

    }
}