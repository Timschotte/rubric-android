package be.hogent.tile3.rubricapplication.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import be.hogent.tile3.rubricapplication.model.Student
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LeerlingSelectVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: LeerlingSelectViewModel

    @Before
    fun setUp() {
        viewModel = LeerlingSelectViewModel(1, 1);
    }

    @Test
    fun fetchNavigateToRubricView_ShouldReturnExpectedValue() {
        val expected = Student(1,"Test","120","Test","20190101","456987")
        val livedata = viewModel.navigateToRubricView

        viewModel.onStudentClicked(expected)
        Assert.assertEquals(expected, livedata.value)

    }

}
