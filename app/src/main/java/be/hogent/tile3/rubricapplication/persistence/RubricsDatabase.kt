package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import androidx.room.*
import be.hogent.tile3.rubricapplication.dao.*
import be.hogent.tile3.rubricapplication.model.*

/**
 * The Room database for entities: [Rubric], [Criterium], [Niveau], [Evaluatie], [CriteriumEvaluatie],
 * [OpleidingsOnderdeel], [Docent], [Student] and [StudentOpleidingsOnderdeel]. This Database class provides following
 * DataAccessObject instances: [RubricDao], [CriteriumDao], [NiveauDao], [EvaluatieDao], [CriteriumEvaluatieDao],
 *  [OpleidingsOnderdeelDao], [DocentDao], [StudentDao] and [StudentOpleidingsOnderdeel]
 *  @property INSTANCE Singleton for Database-object
 * @see RoomDatabase
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
    version = 28, exportSchema = false)
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

        /**
         * This function returns an instance of [RubricsDatabase]. If the database already exists, this instance will
         *  be returned. If database doesn't exists, it will be created and returned.
         *  @param context [Context]
         *  @return [RubricsDatabase]
         */
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