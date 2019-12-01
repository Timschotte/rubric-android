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

class DocentDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        val olod = TestUtils.createMockOpleidingsOnderdeel()
        rubricDatabase.opleidingsOnderdeelDao().insert(olod)

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertDocentenSavesData() {
        val docenten = TestUtils.createMockDocenten()
        docenten.forEach {
            rubricDatabase.docentDao().insert(it)
        }

        val retrievedDocenten = TestUtils.getValue(rubricDatabase.docentDao().getAll())
        assert(retrievedDocenten.isNotEmpty())
    }

    @Test
    fun getDocentenDatabaseRetrievesData() {
        val docenten = TestUtils.createMockDocenten()
        docenten.forEach {
            rubricDatabase.docentDao().insert(it)
        }

        val retrievedDocenten = TestUtils.getValue(rubricDatabase.docentDao().getAll())
        assert(retrievedDocenten == docenten.sortedWith(compareBy({it.docentId} , {it.docentId})))
    }

    @Test
    fun clearDocentenDatabase() {
        val docenten = TestUtils.createMockDocenten()
        docenten.forEach {
            rubricDatabase.docentDao().insert(it)
        }

        val retrievedDocenten = TestUtils.getValue(rubricDatabase.docentDao().getAll())
        assert(retrievedDocenten.isEmpty())
    }

}