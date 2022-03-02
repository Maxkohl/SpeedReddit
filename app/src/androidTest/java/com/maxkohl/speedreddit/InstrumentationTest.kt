package com.maxkohl.speedreddit

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxkohl.speedreddit.di.RetrofitModule
import com.maxkohl.speedreddit.home.HomeFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(RetrofitModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class InstrumentationTest : BaseTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())


    protected lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.url("/")
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    //homescreen is populated with posts
    @Test
    fun givenArticleIsClickedThenNavigateToDetailsScreen() {
        //Launch fragment
        launchFragmentInHiltContainer<HomeFragment> {}

        waitForView(ViewMatchers.withText("image_post")).check(matches(isDisplayed()))

        //Click on first article
//        onView(withId(R.id.posts_recyclerview)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        //Check that it navigates to Detail screen
//        assertThat(navController.currentDestination?.id).isEqualTo(R.id.)
    }


    //can click content to view post

    //can view post and nivagate back

    //can scroll and click into post
}

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_SpeedReddit,
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
