package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import androidx.room.*
import be.hogent.tile3.rubricapplication.dao.*
import be.hogent.tile3.rubricapplication.model.*

/**
 * The Room database that contains the Rubrics table
 */
@Database(entities = arrayOf(Rubric::class, Criterium::class, Niveau::class, OpleidingsOnderdeel::class, Docent::class, Student::class), version = 3, exportSchema = false)
abstract class RubricsDatabase : RoomDatabase() {
    abstract fun rubricDao(): RubricDao
    abstract fun criteriumDao(): CriteriumDao
    abstract fun niveauDao(): NiveauDao
    abstract fun opleidingsOnderdeelDao(): OpleidingsOnderdeelDao
    abstract fun docentDao(): DocentDao
    abstract fun studentDao(): StudentDao

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