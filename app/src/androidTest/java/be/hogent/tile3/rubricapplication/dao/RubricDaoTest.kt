package be.hogent.tile3.rubricapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import be.hogent.tile3.rubricapplication.TestUtils.createMockOpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.TestUtils.createMockRubrics
import be.hogent.tile3.rubricapplication.TestUtils.getValue
import be.hogent.tile3.rubricapplication.persistence.RubricsDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RubricDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()
        val olod = createMockOpleidingsOnderdeel()
        rubricDatabase.opleidingsOnderdeelDao().insert(olod)
    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertRubricSavesData() {
        val rubrics = createMockRubrics()
        rubrics.forEach {
            rubricDatabase.rubricDao().insert(it)
        }

        val retrievedRubrics = getValue(rubricDatabase.rubricDao().getAllRubrics())
        assert(retrievedRubrics.isNotEmpty())
    }

    @Test
    fun getRubricDatabaseRetrievesData() {
        val rubrics = createMockRubrics()

        rubrics.forEach {
            rubricDatabase.rubricDao().insert(it)
        }

        val retrievedRubrics = getValue(rubricDatabase.rubricDao().getAllRubrics())
        assert(retrievedRubrics == rubrics.sortedWith(compareBy({it.rubricId} , {it.rubricId})))
    }

    @Test
    fun clearRubricDatabase() {
        val rubrics = createMockRubrics()

        rubrics.forEach {
            rubricDatabase.rubricDao().insert(it)
        }

        rubricDatabase.rubricDao().deleteAllRubrics()
        val retrievedRubrics = getValue(rubricDatabase.rubricDao().getAllRubrics())
        assert(retrievedRubrics.isEmpty())
    }


}