package be.hogent.tile3.rubricapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RubricSelectVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: RubricSelectViewModel

    @Before
    fun setUp() {
        viewModel = RubricSelectViewModel(1)
    }

    @Test
    fun fetchNavigateToKlassSelect_ShouldReturnExpectedValue() {
        val expected = "1"
        val livedata = viewModel.navigateToKlasSelect

        viewModel.onRubricClicked(expected)
        assertEquals(expected,livedata.value)

    }

}