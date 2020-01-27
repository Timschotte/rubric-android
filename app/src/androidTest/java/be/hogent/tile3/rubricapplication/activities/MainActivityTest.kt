package be.hogent.tile3.rubricapplication.activities

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import be.hogent.tile3.rubricapplication.R

import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java
    )

    @Test
    fun hostFragment_isDisplayedAtStart() {
        onView(withId(R.id.myNavHostFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun clickStartButton_DisplaysSelectOpleiding() {
        onView(withId(R.id.start_evaluatie_button)).perform(ViewActions.click())

        onView(withId(R.id.opleiding_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun selectOpleidings_DisplaysRubrics() {
        onView(withId(R.id.start_evaluatie_button)).perform(ViewActions.click())
        onView(withId(R.id.opleidingenList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.rubricList)).check(matches(isDisplayed()))
    }

    @Test
    fun selectRubric_DisplaysStudents() {
        onView(withId(R.id.start_evaluatie_button)).perform(ViewActions.click())
        onView(withId(R.id.opleidingenList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.rubricList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.leerlingList)).check(matches(isDisplayed()))
    }

    @Test
    fun selectRubric_DisplaysEvaluatie() {
        onView(withId(R.id.start_evaluatie_button)).perform(ViewActions.click())
        onView(withId(R.id.opleidingenList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.rubricList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.leerlingList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.criteriumOverzichtFragmentWrapper)).check(matches(isDisplayed()))
    }


}