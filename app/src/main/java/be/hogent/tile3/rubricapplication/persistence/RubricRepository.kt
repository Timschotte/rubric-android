package be.hogent.tile3.rubricapplication.persistence

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


/**
 * This class is used to run queries for the Rubric Objects
 */
class RubricRepository(
    private val rubricDao: RubricDao,
    private val criteriumDao: CriteriumDao,
    private val niveauDao: NiveauDao
) {

    @Inject
    lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    fun getAllRubricsFromOpleidingsOnderdeel(id: Long): LiveData<List<Rubric>> {
        return rubricDao.getAllRubricsFromOpleidingsOnderdeel(id)
    }

    fun getEvaluatieRubric(rubricId: Long): EvaluatieRubric {
        return rubricDao.getEvaluatieRubric(rubricId)
    }

    suspend fun refreshRubrics() {
        try {
            val rubrics = rubricApi.getRubrics("IN_GEBRUIK").await().toMutableList()
            rubrics.addAll(rubricApi.getRubrics("PUBLIEK").await())
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
            rubrics.map {
                Log.i("Test", it.omschrijving + "from refreshRubric in repository")
            }

            } catch (e: IOException) {
                Log.i("RubricRepository", e.message)
            }
        }
    }