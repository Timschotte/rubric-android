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

class StudentOpleidingsOnderdeelDaoTest {

    private lateinit var rubricDatabase: RubricsDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        rubricDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RubricsDatabase::class.java).allowMainThreadQueries().build()

        rubricDatabase.opleidingsOnderdeelDao().insert(TestUtils.createMockOpleidingsOnderdeel())

        val students = TestUtils.createMockStudenten()
        students.forEach {
            rubricDatabase.studentDao().insert(it)
        }

    }

    @After
    fun closeDb() {
        rubricDatabase.close()
    }

    @Test
    fun insertStudentOpleidingsOnderdelenSavesData() {
        val studenOlods = TestUtils.createMockStudentOpleidingsOnderdelen()
        studenOlods.forEach {
            rubricDatabase.studentOpleidingsOnderdeelDao().insert(it)
        }

        val retrievedStudentenOlods = TestUtils.getValue(rubricDatabase.studentOpleidingsOnderdeelDao().getStudentenFromOpleidingsOnderdeel(1))
        assert(retrievedStudentenOlods.isNotEmpty())
    }

    @Test
    fun getStudentOpleidingsOnderdelenDatabaseRetrievesData() {
        val studenOlods = TestUtils.createMockStudentOpleidingsOnderdelen()
        studenOlods.forEach {
            rubricDatabase.studentOpleidingsOnderdeelDao().insert(it)
        }

        val retrievedStudentenOlods = TestUtils.getValue(rubricDatabase.studentOpleidingsOnderdeelDao().getStudentenFromOpleidingsOnderdeel(1))
        assert(retrievedStudentenOlods == studenOlods.sortedWith(compareBy({it.studentId} , {it.studentId})))
    }

    @Test
    fun clearStudentOpleidingsOnderdelenDatabase() {
        val studenOlods = TestUtils.createMockStudentOpleidingsOnderdelen()
        studenOlods.forEach {
            rubricDatabase.studentOpleidingsOnderdeelDao().insert(it)
        }

        val retrievedStudentenOlods = TestUtils.getValue(rubricDatabase.studentOpleidingsOnderdeelDao().getStudentenFromOpleidingsOnderdeel(1))
        assert(retrievedStudentenOlods.isEmpty())
    }

}