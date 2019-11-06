package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import android.os.AsyncTask
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import be.hogent.tile3.rubricapplication.dao.*
import be.hogent.tile3.rubricapplication.model.*

/**
 * The Room database that contains the Rubrics table
 */
@Database(entities = arrayOf(Rubric::class, Criterium::class, Niveau::class, Opleiding::class, Docent::class, Student::class), version = 2, exportSchema = false)
abstract class RubricsDatabase : RoomDatabase() {
    abstract fun rubricDao(): RubricDao
    abstract fun criteriumDao(): CriteriumDao
    abstract fun niveauDao(): NiveauDao
    abstract fun opleidingDao(): OpleidingDao
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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RubricsDatabase::class.java,
                    "Rubrics_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}