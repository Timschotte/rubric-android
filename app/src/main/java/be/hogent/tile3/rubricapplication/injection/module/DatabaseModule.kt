package be.hogent.tile3.rubricapplication.injection.module

import android.app.Application
import android.content.Context
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import be.hogent.tile3.rubricapplication.persistence.RubricsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provides the Database Service implemenation
 * @param application the applicationContext used to instantiate the service
 */
@Module
class DatabaseModule(private val application: Application) {

    /**
     * Shows how to create a RubricRepository
     *  @param rubricDao the RubricDao used to instantiate the Repository
     */
    @Provides
    @Singleton
    internal fun provideRubricRepository(rubricDao: RubricDao,
                                         criteriumDao: CriteriumDao,
                                         niveauDao: NiveauDao): RubricRepository {
        return RubricRepository(rubricDao, criteriumDao, niveauDao)
    }

    /**
     * Shows how to create a CriteriumRepository
     *  @param criteriumDao the CriteriumDao used to instantiate the Repository
     */
    @Provides
    @Singleton
    internal fun provideCriteriumRepository(criteriumDao: CriteriumDao): CriteriumRepository {
        return CriteriumRepository(criteriumDao)
    }

    /**
     * Shows how to create a NiveauRepository
     *  @param niveauDao the NiveauDao used to instantiate the Repository
     */
    @Provides
    @Singleton
    internal fun provideNiveauRepository(niveauDao: NiveauDao): NiveauRepository {
        return NiveauRepository(niveauDao)
    }



    /**
     * Shows how to create a RubricDao
     *  @param rubricsDatabase the RubricsDatabase used to instantiate the Dao
     */
    @Provides
    @Singleton
    internal fun provideRubricDao(rubricsDatabase: RubricsDatabase): RubricDao {
        return rubricsDatabase.rubricDao()
    }

    /**
     * Shows how to create a CriteriumDao
     *  @param rubricsDatabase the RubricsDatabase used to instantiate the Dao
     */
    @Provides
    @Singleton
    internal fun provideCriteriumDao(rubricsDatabase: RubricsDatabase): CriteriumDao {
        return rubricsDatabase.criteriumDao()
    }

    /**
     * Shows how to create a NiveauDao
     *  @param rubricsDatabase the RubricsDatabase used to instantiate the Dao
     */
    @Provides
    @Singleton
    internal fun provideNiveauDao(rubricsDatabase: RubricsDatabase): NiveauDao {
        return rubricsDatabase.niveauDao()
    }

    /**
     * Shows how to create a rubricsDatabase
     *  @param context the Context used to instantiate the Database
     */
    @Provides
    @Singleton
    internal fun provideRubricsDatabase(context: Context): RubricsDatabase {
        return RubricsDatabase.getDatabase(context)
    }

    /**
     * Shows how to create an ApplicationContext
     */
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

}