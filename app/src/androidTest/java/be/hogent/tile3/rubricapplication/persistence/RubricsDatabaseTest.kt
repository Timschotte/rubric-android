package be.hogent.tile3.rubricapplication.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import be.hogent.tile3.rubricapplication.TestUtils.getValue
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RubricsDatabaseTest{
    private lateinit var rubricDao: RubricDao
    private lateinit var criteriumDao: CriteriumDao
    private lateinit var niveauDao: NiveauDao
    private lateinit var db: RubricsDatabase

    private val rubric1 = Rubric("1","Android","Het vak native apps I", "2019-10-28T19:10:44.170103","2019-10-28T19:10:44.170103")
    private val rubric2 = Rubric("2","IOS","Het vak native apps II", "2019-10-28T19:10:44.170103","2019-10-28T19:10:44.170103")
    private val rubric3 = Rubric("3","Projecten","Het vak native apps III", "2019-10-28T19:10:44.170103","2019-10-28T19:10:44.170103")

    private val criterium1 = Criterium(
        "1",
        "Dependency Injection",
        "waarom",
        20.0,
        "1"
    )
    private val criterium2 = Criterium(
        "3",
        "Testen",
        "Omdat het moet",
        40.0,
        "1"
    )
    private val criterium3 = Criterium(
        "4",
        "Gebruik geziene materie",
        "Waarom schrijf ik anders een cursus",
        40.0,
        "1"
    )

    private val niveau1 = Niveau(
        "1",
        "Slecht",
        "De student kan er niets van",
        0,
        2,
        -2,
        "1"
    )

    private val niveau2 = Niveau(
        "2",
        "Matig",
        "De heeft al van het begrip gehoord",
        3,
        4,
        -1,
        "1"
    )
    private val niveau3 = Niveau(
        "3",
        "Gemiddeld",
        "De student kan het begrip verklaren",
        5,
        6,
        0,
        "1"
    )
    private val niveau4 = Niveau(
        "4",
        "Bovengemiddeld",
        "De student kan het begrip verklaren en begrijpt het",
        6,
        7,
        1,
        "1"
    )
    private val niveau5 = Niveau(
        "5",
        "Super",
        "De student kan het begrip verklaren en snapt de context",
        8,
        10,
        2,
        "1"
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        rubricDao = db.rubricDao()
        criteriumDao = db.criteriumDao()
        niveauDao = db.niveauDao()

        rubricDao.insert(rubric1)
        rubricDao.insert(rubric2)
        rubricDao.insert(rubric3)

        criteriumDao.insert(criterium1)
        criteriumDao.insert(criterium2)
        criteriumDao.insert(criterium3)

        niveauDao.insert(niveau1)
        niveauDao.insert(niveau2)
        niveauDao.insert(niveau3)
        niveauDao.insert(niveau4)
        niveauDao.insert(niveau5)
    }

    @Test
    fun insertRubrics_returnAllRubrics(){
        val rubrics = getValue(rubricDao.getAllRubrics())
        assertEquals(3, rubrics.size)
    }

    @Test
    fun insertCriteria_returnsAllCriteria(){
        val criteria = getValue(criteriumDao.getAllCriteria())
        assertEquals(3, criteria.size)
    }

    @Test
    fun insertNiveaus_returnsAllNiveaus(){
        val niveaus = getValue(niveauDao.getAllNiveaus())
        assertEquals(5, niveaus.size)
    }



    @After
    fun breakDown(){
        db.close()
    }

}