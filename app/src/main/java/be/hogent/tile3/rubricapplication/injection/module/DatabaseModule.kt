package be.hogent.tile3.rubricapplication.injection.module

import android.app.Application
import android.content.Context
import be.hogent.tile3.rubricapplication.dao.*
import be.hogent.tile3.rubricapplication.persistence.*
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

    @Provides
    @Singleton
    internal fun provideOpleidingsOnderdeelRepository(opleidingsOnderdeelDao: OpleidingsOnderdeelDao): OpleidingsOnderdeelRepository {
        return OpleidingsOnderdeelRepository(opleidingsOnderdeelDao)
    }

    @Provides
    @Singleton
    internal fun provideStudentRepository(studentDao: StudentDao, studentOpleidingsOnderdeelDao: StudentOpleidingsOnderdeelDao): StudentRepository {
        return StudentRepository(studentDao, studentOpleidingsOnderdeelDao)
    }



    /**
     * Shows how to create a EvaluatieRepository
     *  @param evaluatieDao the EvaluatieDao used to instantiate the Repository
     */
    @Provides
    @Singleton
    internal fun provideEvaluatieRepository(evaluatieDao: EvaluatieDao, criteriumEvaluatieDao: CriteriumEvaluatieDao): EvaluatieRepository {
        return EvaluatieRepository(evaluatieDao, criteriumEvaluatieDao)
    }

    /**
     * Shows how to create a CriteriumEvaluatieRepository
     *  @param criteriumEvaluatieDao the CriteriumEvaluatieDao used to instantiate the Repository
     */
    @Provides
    @Singleton
    internal fun provideCriteriumEvaluatieRepository(criteriumEvaluatieDao: CriteriumEvaluatieDao): CriteriumEvaluatieRepository {
        return CriteriumEvaluatieRepository(criteriumEvaluatieDao)
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

    @Provides
    @Singleton
    internal fun provideOpleidingsOnderdeelDao(rubricsDatabase: RubricsDatabase): OpleidingsOnderdeelDao {
        return rubricsDatabase.opleidingsOnderdeelDao()
    }

    @Provides
    @Singleton
    internal fun provideStudentDao(rubricsDatabase: RubricsDatabase): StudentDao {
        return rubricsDatabase.studentDao()
    }

    @Provides
    @Singleton
    internal fun provideStudentOpleidingsOnderdeelDao(rubricsDatabase: RubricsDatabase): StudentOpleidingsOnderdeelDao {
        return rubricsDatabase.studentOpleidingsOnderdeelDao()
    }


    /**
     * Shows how to create a EvaluatieDao
     *  @param rubricsDatabase the RubricsDatabase used to instantiate the Dao
     */
    @Provides
    @Singleton
    internal fun provideEvaluatieDao(rubricsDatabase: RubricsDatabase): EvaluatieDao {
        return rubricsDatabase.evaluatieDao()
    }

    /**
     * Shows how to create a CriteriumEvaluatieDao
     *  @param rubricsDatabase the RubricsDatabase used to instantiate the Dao
     */
    @Provides
    @Singleton
    internal fun provideCriteriumEvaluatieDao(rubricsDatabase: RubricsDatabase): CriteriumEvaluatieDao {
        return rubricsDatabase.criteriumEvaluatieDao()
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