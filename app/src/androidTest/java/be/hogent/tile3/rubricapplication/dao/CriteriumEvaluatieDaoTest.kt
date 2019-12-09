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

class CriteriumEvaluatieDaoTest {

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

        val evals = TestUtils.createMockEvaluaties()
        evals.forEach {
            rubricDatabase.evaluatieDao().insert(it)
        }

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertCriteriumEvaluatieSavesData() {
        val criteriumEvals = TestUtils.createMockCriteriumEvaluaties()
        criteriumEvals.forEach {
            rubricDatabase.criteriumEvaluatieDao().insert(it)
        }

        val retrievedCriteriumEvals = TestUtils.getValue(rubricDatabase.criteriumEvaluatieDao().getAll())
        assert(retrievedCriteriumEvals.isNotEmpty())
    }

    @Test
    fun getCrtieriumEvaluatieDatabaseRetrievesData() {
        val criteriumEvals = TestUtils.createMockCriteriumEvaluaties()
        criteriumEvals.forEach {
            rubricDatabase.criteriumEvaluatieDao().insert(it)
        }

        val retrievedCriteriumEvals = TestUtils.getValue(rubricDatabase.criteriumEvaluatieDao().getAll())
        assert(retrievedCriteriumEvals == criteriumEvals.sortedWith(compareBy({it.criteriumEvaluatieId} , {it.criteriumEvaluatieId})))
    }

    @Test
    fun clearCriteriumEvaluatieDatabase() {
        val criteriumEvals = TestUtils.createMockCriteriumEvaluaties()
        criteriumEvals.forEach {
            rubricDatabase.criteriumEvaluatieDao().insert(it)
        }

        val retrievedCriteriumEvals = TestUtils.getValue(rubricDatabase.criteriumEvaluatieDao().getAll())
        assert(retrievedCriteriumEvals.isEmpty())
    }

}