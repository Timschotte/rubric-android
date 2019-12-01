package be.hogent.tile3.rubricapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import be.hogent.tile3.rubricapplication.TestUtils.getValue
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
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
        CreateMockOpleidingsOnderdeel()
    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    fun CreateMockOpleidingsOnderdeel() {
        val olod = OpleidingsOnderdeel(1, "Toegepaste Informatica")
        rubricDatabase.opleidingsOnderdeelDao().insert(olod)
    }

    fun CreateMockRubrics() : List<Rubric> {
        val rubricList = ArrayList<Rubric>()

        val rubric1 = Rubric("1", "Android", "Het vak native apps I", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)
        val rubric2 = Rubric("2", "IOS", "Het vak native apps II", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)
        val rubric3 = Rubric("3", "Programmeren 3", "Het vak programmeren III", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)

        rubricList.add(rubric1)
        rubricList.add(rubric2)
        rubricList.add(rubric3)

        return rubricList
    }

    @Test
    fun insertRubricSavesData() {
        val rubrics = CreateMockRubrics()
        rubrics.forEach {
            rubricDatabase.rubricDao().insert(it)
        }

        val retrievedRubrics = getValue(rubricDatabase.rubricDao().getAllRubrics())
        assert(retrievedRubrics.isNotEmpty())
    }

    @Test
    fun getRubricDatabaseRetrievesData() {
        val rubrics = CreateMockRubrics()

        rubrics.forEach {
            rubricDatabase.rubricDao().insert(it)
        }

        val retrievedRubrics = getValue(rubricDatabase.rubricDao().getAllRubrics())
        assert(retrievedRubrics == rubrics.sortedWith(compareBy({it.rubricId} , {it.rubricId})))
    }

    @Test
    fun clearRubricDatabase() {
        val rubrics = CreateMockRubrics()

        rubrics.forEach {
            rubricDatabase.rubricDao().insert(it)
        }

        rubricDatabase.rubricDao().deleteAllRubrics()
        val retrievedRubrics = getValue(rubricDatabase.rubricDao().getAllRubrics())
        assert(retrievedRubrics.isEmpty())
    }


}