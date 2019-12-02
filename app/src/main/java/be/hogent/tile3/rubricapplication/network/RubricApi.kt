package be.hogent.tile3.rubricapplication.network

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*
import kotlin.reflect.jvm.internal.impl.types.checker.NewCapturedType

interface RubricApi{

//    @GET("rubric/{rubricId}")
//    fun getRubric(@Path("rubricId") id: Int): Observable<RubricData>

    @GET("rubric?status=PUBLIEK")
    fun getRubrics(): Deferred<List<NetworkRubric>>

    @GET("opleidingsonderdeel")
    fun getOpleidingsOnderdeel(): Deferred<List<NetworkOpleidingsOnderdeel>>

    @GET("student")
    fun getStudenten(): Deferred<List<NetworkStudent>>

    @POST("evaluatie")
    fun saveEvaluatie(@Body evaluatieRubric: NetworkRubricEvaluatie): Call<NetworkRubricEvaluatie>

    @PUT("evaluatie")
    fun updateEvaluatie(@Body evaluatieRubric: NetworkRubricEvaluatie): Call<NetworkRubricEvaluatie>

    @GET("evaluatie/?rubricId={rubricId}&docentId={docentId}&studentId={studentId}")
    fun getEvaluatie(
        @Path("rubricId")rubricId: Long,
        @Path("docentId")docentId: Int,
        @Path("studentId") studentId: Long): Call<NetworkRubricEvaluatie?>

}