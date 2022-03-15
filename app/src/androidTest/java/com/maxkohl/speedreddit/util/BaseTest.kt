package com.maxkohl.speedreddit.util

import android.view.View
import androidx.test.espresso.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher
import java.lang.Exception

open class BaseTest {

    companion object {
        fun lookFor(matcher: Matcher<View>): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return ViewMatchers.isRoot()
                }

                override fun getDescription(): String {
                    return "Looking for $matcher"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    var attempts = 0
                    val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)
                    childViews.forEach {
                        attempts++
                        if (matcher.matches(it)) {
                            return
                        }
                    }

                    throw NoMatchingViewException.Builder()
                        .withRootView(view)
                        .withViewMatcher(matcher)
                        .build()
                }
            }
        }
    }


    fun waitForView(matcher: Matcher<View>,
                    timeoutMillis: Int = 5000,
                    attemptTimeoutMillis: Long = 100
    ): ViewInteraction {
        val maxAttempts = timeoutMillis / attemptTimeoutMillis.toInt()
        var attempts = 0
        for (i in 0..maxAttempts) {
            try {
                attempts++
                Espresso.onView(ViewMatchers.isRoot()).perform(lookFor(matcher))
                return Espresso.onView(matcher)
            } catch (e: Exception) {
                if (attempts == maxAttempts) {
                    throw e
                }
                Thread.sleep(attemptTimeoutMillis)
            }
        }
        throw Exception("Could not find a view matching $matcher")
    }
}