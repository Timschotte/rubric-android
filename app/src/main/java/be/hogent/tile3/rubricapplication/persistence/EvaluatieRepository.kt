package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.network.*
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EvaluatieRepository(private val evaluatieDao: EvaluatieDao, private val criteriumEvaluatieDao: CriteriumEvaluatieDao, private val rubricApi: RubricApi){
    suspend fun insert(evaluatie: Evaluatie){
        return withContext(Dispatchers.IO){
            evaluatieDao.insert(evaluatie)
        }
    }

    fun insertTemp(evaluatie: Evaluatie){
            evaluatieDao.insert(evaluatie)
    }

    suspend fun update(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.update(evaluatie)
        }
    }

    suspend fun get(evaluatieId: String): Evaluatie?{
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieDao.get(evaluatieId)
            evaluatie
        }
    }

    suspend fun delete(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.delete(evaluatie)
        }
    }

    suspend fun getByRubricAndStudent(rubricId: String, studentId: Long): Evaluatie? {
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieDao.getByRubricAndStudent(rubricId, studentId)
            evaluatie
        }
    }

    suspend fun verwijderVorigeTempEvaluatie() {
            criteriumEvaluatieDao.verwijderTempCriteriumEvaluaties()
            evaluatieDao.verwijderTempEvaluatie()
    }

    /**
     * Deze functie zal een tijdelijke evaluatie en bijhorende criteriumevaluaties persisteren tot een permanente evaluatie
     */
    suspend fun persisteerTemp(evaluatie: Evaluatie) {
        //Als er één bestaat, moet de GUID overgenomeen worden
        //Als er geen bestaat, moet een nieuwe GUID
        withContext(Dispatchers.IO){
            val bestaandeEvaluatie = evaluatieDao.getByRubricAndStudent(evaluatie.rubricId, evaluatie.studentId)
            bestaandeEvaluatie?.let {
                criteriumEvaluatieDao.verwijderBestaandeCriteriumEvaluaties(it.evaluatieId)
                evaluatieDao.delete(it)
            }
            persisteerEvaluatieToService(evaluatie)

            evaluatie.evaluatieId = UUID.randomUUID().toString()
            evaluatieDao.insert(evaluatie)

            criteriumEvaluatieDao.koppelTempAanNieuw(evaluatie.evaluatieId)

            verwijderVorigeTempEvaluatie()

            val checkResult = evaluatieDao.get(evaluatie.evaluatieId)

            println(checkResult?.evaluatieId)
        }

    }

    fun persisteerEvaluatieToService(evaluatie: Evaluatie) {

        evaluatie?.let {
            try {
                it.criteriumEvaluaties = criteriumEvaluatieDao.getAllForEvaluatie(it.evaluatieId)
                var networkRubricEvaluatie: NetworkRubricEvaluatie = it.asNetworkModel()

                //TODO Api Backend call opvragen evaluatie met studentId, rubricId, docentId
                /*
                // Kijken of er een bestaande evaluatie reeds bestaat voor de gekozen student en rubric voor de docent
                rubricApi.getEvaluatie(networkRubricEvaluatie.rubricId!!, networkRubricEvaluatie.docentId!!, networkRubricEvaluatie.studentId!!).enqueue(object : Callback<NetworkRubricEvaluatie?> {
                    override fun onFailure(call: Call<NetworkRubricEvaluatie?>, t: Throwable) {
                        Log.i("TestN", "API - Error: " + t.message)
                    }
                    override fun onResponse(call: Call<NetworkRubricEvaluatie?>, response: Response<NetworkRubricEvaluatie?>) {
                        // Als response een NetworkRubricEvaluatie object is: bestaande evaluatie gevonden > PUT (update)
                        response.body()?.let {
                            Log.i("TestN", "API - Evaluatie found on service")
                            Log.i("TestN", "API - PUT evaluatie to service")
                            rubricApi.updateEvaluatie(networkRubricEvaluatie)
                                .enqueue(object : Callback<NetworkRubricEvaluatie> {
                                    override fun onFailure(call: Call<NetworkRubricEvaluatie>, t: Throwable) {
                                        Log.i("TestN", "API - Error: " + t.message)
                                    }

                                    override fun onResponse(call: Call<NetworkRubricEvaluatie>, response: Response<NetworkRubricEvaluatie>) {
                                        Log.i("TestN", "API - Response: " + response.code() + " (code) - " + response.isSuccessful + " (isSucces) - " + response.body() + " (message)")
                                    }
                                })
                        }
                        //todo api response controleren
                        // Als response geen NetworkRubricEvaluatie object is > geen bestaande evaluatie > POST (new)
                        if (!response.isSuccessful) {
                            Log.i("TestN", "API - No evaluatie found on service")
                            Log.i("TestN", "API - POST evaluatie to service")
                            rubricApi.saveEvaluatie(networkRubricEvaluatie)
                                .enqueue(object : Callback<NetworkRubricEvaluatie> {
                                    override fun onFailure(call: Call<NetworkRubricEvaluatie>, t: Throwable) {
                                        Log.i("TestN", "API - Error: " + t.message)
                                    }
                                    override fun onResponse(call: Call<NetworkRubricEvaluatie>, response: Response<NetworkRubricEvaluatie>) {
                                        Log.i("TestN", "API - Response: " + response.code() + " (code) - " + response.isSuccessful + " (isSucces) - " + response.body() + " (message)")
                                    }
                                })
                        }
                    }
                })*/
                // TEST
                // id's mogen niet in request (voor POST nieuwe evaluatie) > null
                Log.i("TestN", "API - POST evaluatie to service")
                networkRubricEvaluatie.id = null
                networkRubricEvaluatie.criteriumEvaluaties.forEach { ce -> ce.id = null }
                rubricApi.saveEvaluatie(networkRubricEvaluatie)
                    .enqueue(object : Callback<NetworkRubricEvaluatie> {
                        override fun onFailure(call: Call<NetworkRubricEvaluatie>, t: Throwable) {
                            Log.i("TestN", "API - Error: " + t.message)
                        }
                        override fun onResponse(call: Call<NetworkRubricEvaluatie>, response: Response<NetworkRubricEvaluatie>) {
                            Log.i("TestN", "GSON: "+networkRubricEvaluatie.logJson())
                            Log.i("TestN", "API - Response: " + response.code() + " (code) - " + response.isSuccessful + " (isSucces) - " + response.body() + " (message)")
                        }
                    })
            } catch (e: Exception) {
                Log.i("TestN", "API - Try Exception: " + e.message)
            }
        }
    }

    fun createTempFromBestaande(bestaandeEvaluatie: Evaluatie): Evaluatie {
        val originalId = bestaandeEvaluatie.evaluatieId

        bestaandeEvaluatie.evaluatieId = TEMP_EVALUATIE_ID
        evaluatieDao.insert(bestaandeEvaluatie)

        val criteriumEvaluaties = criteriumEvaluatieDao.getAllForEvaluatie(originalId)

        criteriumEvaluaties.forEach { it.evaluatieId = TEMP_EVALUATIE_ID }

        criteriumEvaluatieDao.insertAll(criteriumEvaluaties)

        return bestaandeEvaluatie

    }
}