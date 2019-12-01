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

class OpleidingsOnderdeelDaoTest {

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
    fun insertOpleidingsOnderdelenSavesData() {
        val olods = TestUtils.createMockOpleidingsOnderdelen()
        olods.forEach {
            rubricDatabase.opleidingsOnderdeelDao().insert(it)
        }

        val retrievedOlods = TestUtils.getValue(rubricDatabase.opleidingsOnderdeelDao().getAll())
        assert(retrievedOlods.isNotEmpty())
    }

    @Test
    fun getOpleidingsOnderdeelDatabaseRetrievesData() {
        val olods = TestUtils.createMockOpleidingsOnderdelen()
        olods.forEach {
            rubricDatabase.opleidingsOnderdeelDao().insert(it)
        }

        val retrievedOlods = TestUtils.getValue(rubricDatabase.opleidingsOnderdeelDao().getAll())
        assert(retrievedOlods == olods.sortedWith(compareBy({it.id} , {it.id})))
    }

    @Test
    fun clearOpleidingsOnderdeelDatabase() {
        val olods = TestUtils.createMockOpleidingsOnderdelen()
        olods.forEach {
            rubricDatabase.opleidingsOnderdeelDao().insert(it)
        }

        val retrievedOlods = TestUtils.getValue(rubricDatabase.opleidingsOnderdeelDao().getAll())
        assert(retrievedOlods.isEmpty())
    }

}