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

class EvaluatieDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        rubricDatabase.opleidingsOnderdeelDao().insert(TestUtils.createMockOpleidingsOnderdeel())

        rubricDatabase.rubricDao().insert(TestUtils.createMockRubric())

        val studenten = TestUtils.createMockStudenten()
        studenten.forEach {
            rubricDatabase.studentDao().insert(it)
        }

        val crits = TestUtils.createMockCriteria()
        crits.forEach {
            rubricDatabase.criteriumDao().insert(it)
        }

        val nivs = TestUtils.createMockNiveaus()
        nivs.forEach {
            rubricDatabase.niveauDao().insert(it)
        }

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertEvaluatieSavesData() {
        val evals = TestUtils.createMockEvaluaties()
        evals.forEach {
            rubricDatabase.evaluatieDao().insert(it)
        }

        val retrievedEvals = TestUtils.getValue(rubricDatabase.evaluatieDao().getAll())
        assert(retrievedEvals.isNotEmpty())
    }

    @Test
    fun getEvaluatieDatabaseRetrievesData() {
        val evals = TestUtils.createMockEvaluaties()
        evals.forEach {
            rubricDatabase.evaluatieDao().insert(it)
        }

        val retrievedEvals = TestUtils.getValue(rubricDatabase.evaluatieDao().getAll())
        assert(retrievedEvals == evals.sortedWith(compareBy({it.evaluatieId} , {it.evaluatieId})))
    }

    @Test
    fun clearEvaluatieDatabase() {
        val evals = TestUtils.createMockEvaluaties()
        evals.forEach {
            rubricDatabase.evaluatieDao().insert(it)
        }

        val retrievedEvals = TestUtils.getValue(rubricDatabase.evaluatieDao().getAll())
        assert(retrievedEvals.isEmpty())
    }

}