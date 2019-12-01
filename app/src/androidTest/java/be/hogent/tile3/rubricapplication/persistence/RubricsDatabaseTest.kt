package be.hogent.tile3.rubricapplication.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import be.hogent.tile3.rubricapplication.TestUtils.getValue
import be.hogent.tile3.rubricapplication.dao.*
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RubricsDatabaseTest{


    private lateinit var criteriumDao: CriteriumDao
    private lateinit var criteriumEvaluatieDao: CriteriumEvaluatieDao
    private lateinit var docentDao: DocentDao
    private lateinit var evaluatieDao: EvaluatieDao
    private lateinit var niveauDao: NiveauDao
    private lateinit var opleidingsOnderdeelDao: OpleidingsOnderdeelDao
    private lateinit var rubricDao : RubricDao
    private lateinit var studentDao: StudentDao
    private lateinit var studentenOpleidingsOnderdeelDao: StudentOpleidingsOnderdeelDao

    private lateinit var db: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        criteriumDao = db.criteriumDao()
        criteriumEvaluatieDao = db.criteriumEvaluatieDao()
        docentDao = db.docentDao()
        evaluatieDao = db.evaluatieDao()
        niveauDao = db.niveauDao()
        opleidingsOnderdeelDao = db.opleidingsOnderdeelDao()
        rubricDao = db.rubricDao()
        studentDao = db.studentDao()
        studentenOpleidingsOnderdeelDao = db.studentOpleidingsOnderdeelDao()


        rubricDao.insert(rubric1)

        criteriumDao.insert(criterium1)
        criteriumDao.insert(criterium2)
        criteriumDao.insert(criterium3)

        niveauDao.insert(niveau1)

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

    // Mock data

    //Rubric 1

    private val opleidingsOnderdeel1 = OpleidingsOnderdeel(1, "Toegepaste Informatica")

    private val rubric1 = Rubric("1", "Android", "Het vak native apps I", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 0)

    private val criterium1 = Criterium("1","Testen", "Omdat het moet", 10.0 , "0", "1")
    private val criterium2 = Criterium("2", "Codekwaliteit", "Gebruikt de student de correcte libraries", 20.0 , "0", "1")
    private val criterium3 = Criterium("3", "Architectuur", "Opbouw van de app", 40.0 , "0", "1")

    private val niveau1 =  Niveau(1, "Slecht", "De student kan er niks van", 0, 5, -2, "1", "0", "1")
    private val niveau2 =  Niveau(2, "Matig", "De student kent het begrip maar pasthet niet toe", 6, 9, -1, "1", "0", "1")
    private val niveau3 =  Niveau(3, "Goed", "De student kan het goed toepassen", 10, 14, 0, "1", "0", "1")
    private val niveau4 =  Niveau(4, "Uitstekend", "De student heeft het volledig onder de knie", 15, 20, 1, "1", "0", "1")

    private val niveau5 =  Niveau(5, "Slecht", "De student kan er niks van", 0, 5, -2, "1", "0", "2")
    private val niveau6 =  Niveau(6, "Matig", "De student kent het begrip maar pasthet niet toe", 6, 9, -1, "1", "0", "2")
    private val niveau7 =  Niveau(7, "Goed", "De student kan het goed toepassen", 10, 14, 0, "1", "0", "2")
    private val niveau8 =  Niveau(8, "Uitstekend", "De student heeft het volledig onder de knie", 15, 20, 1, "1", "0", "2")

    private val niveau9 =  Niveau(1, "Slecht", "De student kan er niks van", 0, 5, -2, "1", "0", "3")
    private val niveau10 =  Niveau(2, "Matig", "De student kent het begrip maar pasthet niet toe", 6, 9, -1, "1", "0", "3")
    private val niveau11 =  Niveau(3, "Goed", "De student kan het goed toepassen", 10, 14, 0, "1", "0", "1")
    private val niveau12 =  Niveau(1, "Uitstekend", "De student heeft het volledig onder de knie", 15, 20, 1, "1", "0", "3")

}