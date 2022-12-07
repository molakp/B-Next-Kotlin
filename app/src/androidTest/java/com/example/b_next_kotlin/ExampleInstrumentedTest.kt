package com.example.b_next_kotlin


import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext: Context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        Assert.assertEquals("com.example.bnext", appContext.packageName)
    }

    @Test
    fun loginButton() {
        onView(withId(R.id.loginButton)) //.perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun inputEmail() {
        onView(withId(R.id.inputEmail)) //.perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun inputPassword() {
        onView(withId(R.id.inputPassword)) //.perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun passwordText() {
        onView(withId(R.id.passwordText)) //.perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun emailText() {
        onView(withId(R.id.emailText)) //.perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun signUpButton() {
        onView(withId(R.id.signUpButton)) //.perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun infoText() {
        onView(withId(R.id.infoText)) //.perform(click())
            .check(matches(isDisplayed()))
    }
}