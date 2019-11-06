package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import android.os.AsyncTask
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric

/**
 * The Room database that contains the Rubrics table
 */
@Database(entities = arrayOf(Rubric::class, Criterium::class, Niveau::class), version = 1, exportSchema = false)
abstract class RubricsDatabase : RoomDatabase() {
    abstract fun rubricDao(): RubricDao
    abstract fun criteriumDao(): CriteriumDao
    abstract fun niveauDao(): NiveauDao

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
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}