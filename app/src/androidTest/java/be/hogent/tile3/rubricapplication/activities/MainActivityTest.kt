package be.hogent.tile3.rubricapplication.activities

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.TestUtils
import be.hogent.tile3.rubricapplication.fragments.LeerlingSelectFragment
import be.hogent.tile3.rubricapplication.fragments.MainMenuFragment
import be.hogent.tile3.rubricapplication.fragments.OpleidingSelectFragment
import be.hogent.tile3.rubricapplication.fragments.RubricSelectFragment
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.persistence.RubricsDatabase
import be.hogent.tile3.rubricapplication.ui.LeerlingSelectViewModel
import be.hogent.tile3.rubricapplication.ui.OpleidingsOnderdeelViewModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.lang.Thread.sleep

class MainActivityTest {

    private lateinit var mockNavController: NavController

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        mockNavController = mock(NavController::class.java)

        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }


    @Test
    fun navigationToOpleidingsList() {

        val scenario = launchFragmentInContainer {
            MainMenuFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever {owner ->
                    if (owner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }

        onView(withId(R.id.start_evaluatie_button)).perform(ViewActions.click())
        verify(mockNavController).navigate(R.id.action_mainMenuFragment_to_opleidingSelectFragment)

    }

    @Test
    fun selectOlod_DisplayRubrics() {

        doAsync{
            val olods = TestUtils.createMockOpleidingsOnderdelen()

            olods.forEach {
                rubricDatabase.opleidingsOnderdeelDao().insert(it)
            }

            val scenario = launchFragmentInContainer {
                OpleidingSelectFragment().also { fragment->
                    fragment.viewLifecycleOwnerLiveData.observeForever {
                        if (it != null) {
                            Navigation.setViewNavController(fragment.requireView(), mockNavController)
                        }
                    }
                }
            }

            onView(withId(R.id.opleidingenList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
            verify(mockNavController).navigate(R.id.action_opleidingSelectFragment_to_rubricSelectFragment)

        }
    }

    @Test
    fun selectRubric_DisplayStudents() {

        doAsync{
            val olod = TestUtils.createMockOpleidingsOnderdeel()
            rubricDatabase.opleidingsOnderdeelDao().insert(olod)

            val rubrics = TestUtils.createMockRubrics()
            rubrics.forEach {
                rubricDatabase.rubricDao().insert(it)
            }

            val args = Bundle().apply {
                putLong("opleidingsOnderdeelId",1)
            }

            val scenario = launchFragmentInContainer(args) {
                RubricSelectFragment().also { fragment->
                    fragment.viewLifecycleOwnerLiveData.observeForever {
                        if (it != null) {
                            Navigation.setViewNavController(fragment.requireView(), mockNavController)
                        }
                    }
                }
            }

            onView(withId(R.id.rubricList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
            verify(mockNavController).navigate(R.id.action_rubricSelectFragment_to_leerlingSelectFragment)

        }
    }

    @Test
    fun selectStudent_DisplayEvaluatie() {

        doAsync{

            val olod = TestUtils.createMockOpleidingsOnderdeel()
            rubricDatabase.opleidingsOnderdeelDao().insert(olod)

            val rubrics = TestUtils.createMockRubrics()
            rubrics.forEach {
                rubricDatabase.rubricDao().insert(it)
            }

            val args = Bundle().apply {
                putLong("opleidingsOnderdeelId",1)
                putString("rubricId","1")
            }

            val scenario = launchFragmentInContainer(args) {
                LeerlingSelectFragment().also { fragment->
                    fragment.viewLifecycleOwnerLiveData.observeForever {
                        if (it != null) {
                            Navigation.setViewNavController(fragment.requireView(), mockNavController)
                        }
                    }
                }
            }

            onView(withId(R.id.leerlingList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
            verify(mockNavController).navigate(R.id.action_leerlingSelectFragment_to_criteriumOverzichtFragment)

        }
    }








}