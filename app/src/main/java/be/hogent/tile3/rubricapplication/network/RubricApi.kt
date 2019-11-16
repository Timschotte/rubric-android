package be.hogent.tile3.rubricapplication.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RubricApi{

//    @GET("rubric/{rubricId}")
//    fun getRubric(@Path("rubricId") id: Int): Observable<RubricData>

    @GET("rubric")
    fun getRubrics(): Deferred<List<NetworkRubric>>

    @GET("opleidingsonderdeel")
    fun getOpleidingsOnderdeel(): Deferred<List<NetworkOpleidingsOnderdeel>>

    @GET("student")
    fun getStudenten(): Deferred<List<NetworkStudent>>


}