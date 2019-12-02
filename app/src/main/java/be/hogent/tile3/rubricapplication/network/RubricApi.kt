package be.hogent.tile3.rubricapplication.network

import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface RubricApi{

    @GET("rubric")
    fun getRubrics(): Deferred<List<NetworkRubric>>

    @GET("opleidingsonderdeel")
    fun getOpleidingsOnderdeel(): Deferred<List<NetworkOpleidingsOnderdeel>>

    @GET("student")
    fun getStudenten(): Deferred<List<NetworkStudent>>

    @GET("evaluatie")
    fun getEvaluaties(@QueryMap params: Map<String, String>) : Deferred<List<NetworkRubricEvaluatie>>
    @GET("evaluatie")
    fun getEvaluatieForId(@Path(value = "id") id: Long): Deferred<NetworkRubricEvaluatie>

    @POST("evaluatie")
    fun postEvaluatie(@Body evaluatie: NetworkRubricEvaluatie): Deferred<NetworkRubricEvaluatie>
    @PUT("evaluatie")
    fun putEvalutatie(@Path(value = "id") id: Long, @Body evaluatie: NetworkRubricEvaluatie): Deferred<NetworkRubricEvaluatie>
}