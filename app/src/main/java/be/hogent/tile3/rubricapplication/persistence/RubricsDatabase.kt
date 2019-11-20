package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import androidx.room.*
import be.hogent.tile3.rubricapplication.dao.*
import be.hogent.tile3.rubricapplication.model.*

/**
 * The Room database that contains the Rubrics table
 */
@Database(entities = arrayOf(Rubric::class,
    Criterium::class,
    Niveau::class,
    Evaluatie::class,
    CriteriumEvaluatie::class,
    OpleidingsOnderdeel::class,
    Docent::class,
    Student::class,
    StudentOpleidingsOnderdeel::class),
    version = 15, exportSchema = false)
abstract class RubricsDatabase : RoomDatabase() {
    abstract fun rubricDao(): RubricDao
    abstract fun criteriumDao(): CriteriumDao
    abstract fun niveauDao(): NiveauDao
    abstract fun evaluatieDao(): EvaluatieDao
    abstract fun criteriumEvaluatieDao(): CriteriumEvaluatieDao
    abstract fun opleidingsOnderdeelDao(): OpleidingsOnderdeelDao
    abstract fun docentDao(): DocentDao
    abstract fun studentDao(): StudentDao
    abstract fun studentOpleidingsOnderdeelDao(): StudentOpleidingsOnderdeelDao

    companion object {
        @Volatile
        private var INSTANCE: RubricsDatabase? = null

        fun getDatabase(context: Context): RubricsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RubricsDatabase::class.java,
                        "Rubrics_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}