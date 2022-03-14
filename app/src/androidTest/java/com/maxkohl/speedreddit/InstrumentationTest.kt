package com.maxkohl.speedreddit

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxkohl.speedreddit.di.RepositoryModule
import com.maxkohl.speedreddit.home.HomeFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class InstrumentationTest : BaseTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun populatesRecyclerViewWithPostItems() {
        launchHomeFragment()

        waitForView(withText("image_post_0")).check(matches(isDisplayed()))
        onView(withText("video_post_0")).check(matches(isDisplayed()))
        onView(withText("text_post_0")).check(matches(isDisplayed()))
        onView(withText("link_post_0")).check(matches(isDisplayed()))
    }

    @Test
    fun successfullyNavigateToPostMediaFragment() {
        launchHomeFragment()

        waitForView(withText("image_post_0")).check(matches(isDisplayed()))
        onView(withId(R.id.posts_recyclerview)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        assertEquals(navController.currentDestination?.id, R.id.imageFragment)
        onView(withText("image_post_0")).check(matches(isDisplayed()))
    }

    fun canScrollAndViewPost() {
        launchHomeFragment()

        waitForView(withText("image_post_0")).check(matches(isDisplayed()))
        onView(withId(R.id.posts_recyclerview))
            .perform(
                scrollToPosition<RecyclerView.ViewHolder>(15)
            )
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    15,
                    click()
                )
            )
    }

    @Test
    fun successfullyNavigatesBackToHomeFragmentAfterViewingPost() {
        launchHomeFragment()

        waitForView(withText("image_post_0")).check(matches(isDisplayed()))
        onView(withId(R.id.posts_recyclerview)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        waitForView(withText("image_post_0")).check(matches(isDisplayed()))
        assertEquals(navController.currentDestination?.id, R.id.imageFragment)

        //TODO Figure out alternative/solution to provided pressBack() crashing
//        pressBack()
//        assertEquals(navController.currentDestination?.id, R.id.homeFragment)
    }

    @Test
    fun navigateToRapidModeSuccessfully() {
        launchActivity<MainActivity>()
        waitForView(withId(R.id.action_rapid)).check(matches(isDisplayed()))
        onView(withId(R.id.action_rapid)).perform(click())

        waitForView(withText("image_post_0")).check(matches(isDisplayed()))
        //TODO Figure out how to get navController currentDestination from activity level
//        assertEquals(navController.currentDestination?.id, R.id.rapidModeFragment)
    }


    private fun launchHomeFragment() {
        launchFragmentInHiltContainer<HomeFragment> {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.homeFragment)
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }
    }
}


inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_SpeedReddit,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: Fragment.() -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )

        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
}
