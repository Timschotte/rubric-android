package be.hogent.tile3.rubricapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import be.hogent.tile3.rubricapplication.TestUtils
import be.hogent.tile3.rubricapplication.TestUtils.createMockCriteria
import be.hogent.tile3.rubricapplication.TestUtils.createMockOpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.TestUtils.createMockRubric
import be.hogent.tile3.rubricapplication.persistence.RubricsDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CriteriumDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        val olod = createMockOpleidingsOnderdeel()
        rubricDatabase.opleidingsOnderdeelDao().insert(olod)

        val rubric = createMockRubric()
        rubricDatabase.rubricDao().insert(rubric)

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertCriteriumSavesData() {
        val criterium = createMockCriteria()
        criterium.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        val retrievedCriterium = TestUtils.getValue(rubricDatabase.criteriumDao().getAllCriteria())
        assert(retrievedCriterium.isNotEmpty())
    }

    @Test
    fun getCrtieriumDatabaseRetrievesData() {
        val criterium = createMockCriteria()
        criterium.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        val retrievedCriterium = TestUtils.getValue(rubricDatabase.criteriumDao().getAllCriteria())
        assert(retrievedCriterium == criterium.sortedWith(compareBy({it.criteriumId} , {it.criteriumId})))
    }

    @Test
    fun clearCriteriumDatabase() {
        val criterium = createMockCriteria()
        criterium.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        rubricDatabase.criteriumDao().deleteAllCriteria()
        val retrievedCriterium = TestUtils.getValue(rubricDatabase.criteriumDao().getAllCriteria())
        assert(retrievedCriterium.isEmpty())
    }
}