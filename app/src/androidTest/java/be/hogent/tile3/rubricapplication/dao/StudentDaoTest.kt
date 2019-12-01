package be.hogent.tile3.rubricapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import be.hogent.tile3.rubricapplication.TestUtils
import be.hogent.tile3.rubricapplication.persistence.RubricsDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StudentDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertStudentenSavesData() {
        val studenten = TestUtils.createMockStudenten()
        studenten.forEach {
            rubricDatabase.studentDao().insert(it)
        }

        val retrievedStudenten = TestUtils.getValue(rubricDatabase.studentDao().getAll())
        assert(retrievedStudenten.isNotEmpty())
    }

    @Test
    fun getStudentenDatabaseRetrievesData() {
        val studenten = TestUtils.createMockStudenten()
        studenten.forEach {
            rubricDatabase.studentDao().insert(it)
        }

        val retrievedStudenten = TestUtils.getValue(rubricDatabase.studentDao().getAll())
        assert(retrievedStudenten == studenten.sortedWith(compareBy({it.studentId} , {it.studentId})))
    }

    @Test
    fun clearStudentenDatabase() {
        val studenten = TestUtils.createMockStudenten()
        studenten.forEach {
            rubricDatabase.studentDao().insert(it)
        }

        val retrievedStudenten = TestUtils.getValue(rubricDatabase.studentDao().getAll())
        assert(retrievedStudenten.isEmpty())
    }

}