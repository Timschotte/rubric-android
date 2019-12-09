package be.hogent.tile3.rubricapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import be.hogent.tile3.rubricapplication.model.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object TestUtils {

    /**
     * Used to check the value inside a LiveData object
     * (c) https://github.com/hdeweirdt/TVShows/blob/foreign_keys/app/src/androidTest/java/com/example/hwei214/tvshows/TestUtils.kt
     */
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }


    fun createMockDocenten(): List<Docent> {
        val docentList = ArrayList<Docent>()

        docentList.add(Docent(1, "Jens Buysse", 1))
        docentList.add(Docent(2, "Chantal Teerlinck", 1))
        docentList.add(Docent(3, "Stijn Lievens", 1))

        return docentList
    }

    fun createMockCriterium(): Criterium {
        return Criterium("1","Testen", "Omdat het moet", 10.0 , "0", "1")
    }

    fun createMockNiveaus(): List<Niveau> {
        val niveauList = ArrayList<Niveau>()

        niveauList.add(Niveau(1,"Slecht","De student kan er niks van",0,5,-2,"1","0","1"))
        niveauList.add(Niveau(2,"Matig","De student begrijpt het begrip maar kan het niet toepassen",6,9,-1,"1","0","1"))
        niveauList.add(Niveau(3,"Goed","De student heeft het voldoende onder de knie",10,15,0,"1","0","1"))
        niveauList.add(Niveau(4,"Uitstekend","De student kan het zeer goed",16,20,1,"1","0","1"))

        return niveauList
    }

    fun createMockOpleidingsOnderdeel(): OpleidingsOnderdeel {
        return OpleidingsOnderdeel(1, "Toegepaste Informatica")
    }

    fun createMockEvaluaties() : List<Evaluatie> {
        val evalList = ArrayList<Evaluatie>()

        evalList.add(Evaluatie("1",1,"1"))
        evalList.add(Evaluatie("2",1,"1"))
        evalList.add(Evaluatie("3",1,"1"))

        return evalList
    }

    fun createMockCriteriumEvaluaties(): List<CriteriumEvaluatie> {
        val critEvals = ArrayList<CriteriumEvaluatie>()

        critEvals.add(CriteriumEvaluatie("1","1","1",3,12,"De student had het voldoende onder de knie"))
        critEvals.add(CriteriumEvaluatie("2","2","2",4,17,"De student was uitstekend in het vak"))
        critEvals.add(CriteriumEvaluatie("3","3","3",1,3,"De student kon er niks van"))

        return critEvals
    }

    fun createMockStudentOpleidingsOnderdelen(): List<StudentOpleidingsOnderdeel> {
        val studentOlods = ArrayList<StudentOpleidingsOnderdeel>()

        studentOlods.add(StudentOpleidingsOnderdeel(1,1))
        studentOlods.add(StudentOpleidingsOnderdeel(2,1))
        studentOlods.add(StudentOpleidingsOnderdeel(3,1))

        return studentOlods
    }

    fun createMockStudenten() : List<Student> {
        val studenten = ArrayList<Student>()

        studenten.add(Student(1,"Gregory House","150-5789"))
        studenten.add(Student(2,"Lisa Cuddy", "897-167"))
        studenten.add(Student(3,"Wilson","789-569"))

        return studenten
    }

    fun createMockOpleidingsOnderdelen() : List<OpleidingsOnderdeel> {
        val olodList = ArrayList<OpleidingsOnderdeel>()

        olodList.add(OpleidingsOnderdeel(1,"Toegepaste informatica"))
        olodList.add(OpleidingsOnderdeel(2,"Verpleegkunde"))
        olodList.add(OpleidingsOnderdeel(3,"Pyschologie en sociale zorgen"))

        return olodList
    }

    fun createMockRubric(): Rubric {
        return Rubric("1", "Android", "Het vak native apps I", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)
    }

    fun createMockRubrics() : List<Rubric> {
        val rubricList = ArrayList<Rubric>()

        val rubric1 = Rubric("1", "Android", "Het vak native apps I", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)
        val rubric2 = Rubric("2", "IOS", "Het vak native apps II", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)
        val rubric3 = Rubric("3", "Programmeren 3", "Het vak programmeren III", "2019-10-28T19:10:44.170103", "2019-10-28T19:10:44.170103", 1)

        rubricList.add(rubric1)
        rubricList.add(rubric2)
        rubricList.add(rubric3)

        return rubricList
    }

    fun createMockCriteria() : List<Criterium> {
        val criteriumList = ArrayList<Criterium>()

        criteriumList.add(Criterium("1","Testen", "Omdat het moet", 10.0 , "0", "1"))
        criteriumList.add(Criterium("2", "Codekwaliteit", "Gebruikt de student de correcte libraries", 20.0 , "0", "1"))
        criteriumList.add(Criterium("3", "Architectuur", "Opbouw van de app", 40.0 , "0", "1"))

        return criteriumList
    }

}