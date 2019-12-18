package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.EvaluatieRubric
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asDatabaseModel
import be.hogent.tile3.rubricapplication.security.AuthStateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


/**
 * Repository for [Rubric] for Room database operations
 * @constructor Creates a [RubricRepository]
 * @property rubricDao DataAccessObject ([RubricDao])for [Rubric]
 * @property criteriumDao DataAccessObject ([CriteriumDao])for Criterium
 * @property niveauDao DataAccessObject ([NiveauDao])for Niveau
 * @property rubricApi API for backend communication
 */
class RubricRepository(
    private val rubricDao: RubricDao,
    private val criteriumDao: CriteriumDao,
    private val niveauDao: NiveauDao

) {

    @Inject
    lateinit var rubricApi: RubricApi
    /**
     * Constructor
     */
    @Inject
    lateinit var context:Context

    private lateinit var authStateManager: AuthStateManager

    init {
        App.component.inject(this)
        authStateManager = AuthStateManager.getInstance(context)
    }
    /**
     * Function for retrieving all [Rubric] for a given OpleidingsOnderdeel from Room database.
     * @param id ID for a given OpleidingsOnderdeel
     * @return [LiveData] [List] of [Rubric]
     * @see RubricDao
     */
    fun getAllRubricsFromOpleidingsOnderdeel(id: Long): LiveData<List<Rubric>> {
        return rubricDao.getAllRubricsFromOpleidingsOnderdeel(id)
    }
    /**
     * Function for retrieving a [EvaluatieRubric] for a given [Rubric] from Room database.
     * @param rubricId ID for a given [Rubric]
     * @return [EvaluatieRubric]
     * @see RubricDao
     */
    fun getEvaluatieRubric(rubricId: Long): EvaluatieRubric {
        return rubricDao.getEvaluatieRubric(rubricId)
    }
    /**
     * Function for synchronizing all [Rubric] from backend API with Room database.
     * @see RubricApi
     * @see RubricDao
     * @see CriteriumDao
     * @see NiveauDao
     */
    suspend fun refreshRubrics() {
        try {
            val rubrics = rubricApi.getRubrics("IN_GEBRUIK", authStateManager.getAuthorizationHeader()).await().toMutableList()
            rubrics.addAll(rubricApi.getRubrics("PUBLIEK", authStateManager.getAuthorizationHeader()).await())
            rubricDao.deleteAllRubrics()
            rubrics.forEach { rubric ->
                rubricDao.insert(rubric.asDatabaseModel())
                rubric.criteriumGroepen.forEach { criteriumGroep ->
                    criteriumGroep.criteria.forEach { networkCriterium ->
                        criteriumDao.insert(
                            networkCriterium.asDatabaseModel(
                                rubric.id,
                                criteriumGroep.id
                            )
                        )
                        networkCriterium.criteriumNiveaus.forEach { networkCriteriumNiveau ->
                            niveauDao.insert(
                                networkCriteriumNiveau.asDatabaseModel(
                                    rubric.id,
                                    criteriumGroep.id,
                                    networkCriterium.id
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Log.i("RubricsLogging", "An error occured while fetching Rubrics from repository")
        }
    }
}