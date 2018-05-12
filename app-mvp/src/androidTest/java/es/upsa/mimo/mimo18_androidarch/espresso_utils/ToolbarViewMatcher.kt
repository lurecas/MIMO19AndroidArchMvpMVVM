package es.upsa.mimo.mimo18_androidarch.espresso_utils

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.Toolbar
import org.hamcrest.Description
import org.hamcrest.Matcher
import android.support.test.espresso.web.model.Atoms.getTitle
import android.support.design.widget.CollapsingToolbarLayout




object CollapsingToolbarViewMatcher {

    fun withCollapsibleToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> {
        return object : BoundedMatcher<Any, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }

            override fun matchesSafely(toolbarLayout: CollapsingToolbarLayout): Boolean {
                return textMatcher.matches(toolbarLayout.title)
            }
        }
    }

}