package be.hogent.tile3.rubricapplication.network

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

interface RubricApi{

    @GET("rubric")
    fun getRubrics(@Query("status") status: String): Deferred<List<NetworkRubric>>

    @GET("opleidingsonderdeel")
    fun getOpleidingsOnderdeel(): Deferred<List<NetworkOpleidingsOnderdeel>>

    @GET("student")
    fun getStudenten(): Deferred<List<NetworkStudent>>

    @GET("student")
    fun getStudenten(@Query("olodId") olodId: Long): Deferred<List<NetworkStudent>>

    @GET("evaluatie")
    fun getEvaluaties(@QueryMap params: Map<String, String>) : Deferred<List<NetworkRubricEvaluatie>>
    @GET("evaluatie")
    fun getEvaluatieForId(@Path(value = "id") id: Long): Deferred<NetworkRubricEvaluatie>

    @POST("evaluatie")
    fun saveEvaluatie(@Body evaluatie: NetworkRubricEvaluatie): Call<NetworkRubricEvaluatie>

    @PUT("evaluatie")
    fun updateEvalutatie(@Path(value = "id") id: Int, @Body evaluatie: NetworkRubricEvaluatie): Call<NetworkRubricEvaluatie>
}