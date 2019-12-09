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

class NiveauDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        // Mock data
        rubricDatabase.opleidingsOnderdeelDao().insert(TestUtils.createMockOpleidingsOnderdeel())

        rubricDatabase.rubricDao().insert(TestUtils.createMockRubric())

        rubricDatabase.criteriumDao().insert(TestUtils.createMockCriterium())

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertNiveauSavesData() {
        val niveaus = TestUtils.createMockNiveaus()
        niveaus.forEach {
            rubricDatabase.niveauDao().insert(it)
        }

        val retrievedNiveaus = TestUtils.getValue(rubricDatabase.niveauDao().getAllNiveaus())
        assert(retrievedNiveaus.isNotEmpty())
    }

    @Test
    fun getNiveauDatabaseRetrievesData() {
        val niveaus = TestUtils.createMockNiveaus()
        niveaus.forEach {
            rubricDatabase.niveauDao().insert(it)
        }

        val retrievedNiveaus = TestUtils.getValue(rubricDatabase.niveauDao().getAllNiveaus())
        assert(retrievedNiveaus == niveaus.sortedWith(compareBy({it.niveauId} , {it.niveauId})))
    }

    @Test
    fun clearNiveauDatabase() {
        val niveaus = TestUtils.createMockNiveaus()
        niveaus.forEach {
            rubricDatabase.niveauDao().insert(it)
        }

        val retrievedNiveaus = TestUtils.getValue(rubricDatabase.niveauDao().getAllNiveaus())
        assert(retrievedNiveaus.isEmpty())
    }

}