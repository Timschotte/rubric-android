package be.hogent.tile3.rubricapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4Builder
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import be.hogent.tile3.rubricapplication.TestUtils
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Rubric
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
    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    fun CreateMockCriterium() : List<Criterium> {
        val criteriumList = ArrayList<Criterium>()

        val criterium1 = Criterium("1","Testen", "Omdat het moet", 10.0 , "0", "1")
        val criterium2 = Criterium("2", "Codekwaliteit", "Gebruikt de student de correcte libraries", 20.0 , "0", "1")
        val criterium3 = Criterium("3", "Architectuur", "Opbouw van de app", 40.0 , "0", "1")

        criteriumList.add(criterium1)
        criteriumList.add(criterium2)
        criteriumList.add(criterium3)

        return criteriumList
    }

    @Test
    fun insertCriteriumSavesData() {
        val criterium = CreateMockCriterium()
        criterium.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        val retrievedCriterium = TestUtils.getValue(rubricDatabase.criteriumDao().getAllCriteria())
        assert(retrievedCriterium.isNotEmpty())
    }

    @Test
    fun getCrtieriumDatabaseRetrievesData() {
        val criterium = CreateMockCriterium()
        criterium.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        val retrievedCriterium = TestUtils.getValue(rubricDatabase.criteriumDao().getAllCriteria())
        assert(retrievedCriterium == criterium.sortedWith(compareBy({it.rubricId} , {it.rubricId})))
    }

    @Test
    fun clearCriteriumDatabase() {
        val criterium = CreateMockCriterium()
        criterium.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        rubricDatabase.criteriumDao().deleteAllCriteria()
        val retrievedCriterium = TestUtils.getValue(rubricDatabase.criteriumDao().getAllCriteria())
        assert(retrievedCriterium.isEmpty())
    }
}