package be.hogent.tile3.rubricapplication.network

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

interface RubricApi{

    @GET("rubric")
    fun getRubrics(@Query("status") status: String, @Header("Authorization") token:String): Deferred<List<NetworkRubric>>

    @GET("opleidingsonderdeel")
    fun getOpleidingsOnderdeel(@Header("Authorization") token:String): Deferred<List<NetworkOpleidingsOnderdeel>>

    @GET("student")
    fun getStudenten(@Header("Authorization") token:String): Deferred<List<NetworkStudent>>

    @GET("student")
    fun getStudenten(@Query("olodId") olodId: Long, @Header("Authorization") token:String): Deferred<List<NetworkStudent>>

    @GET("evaluatie")
    fun getEvaluaties(@QueryMap params: Map<String, String>, @Header("Authorization") token:String) : Deferred<List<NetworkRubricEvaluatie>>
    @GET("evaluatie")
    fun getEvaluatieForId(@Path(value = "id") id: Long, @Header("Authorization") token:String): Deferred<NetworkRubricEvaluatie>

    @POST("evaluatie")
    fun saveEvaluatie(@Body evaluatie: NetworkRubricEvaluatie, @Header("Authorization") token:String): Call<NetworkRubricEvaluatie>

    @PUT("evaluatie/{id}")
    fun updateEvalutatie(@Path(value = "id") id: Int, @Body evaluatie: NetworkRubricEvaluatie,@Header("Authorization") token:String): Call<NetworkRubricEvaluatie>
}