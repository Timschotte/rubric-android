package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.data.RubricData
import be.hogent.tile3.rubricapplication.data.RubricsResource
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface RubricApi{

    @GET("rubric/{rubricId}")
    fun getRubric(@Path("rubricId") id: Int): Observable<RubricData>

    @GET("rubric")
    fun getRubrics(): Deferred<List<NetworkRubric>>
    /*fun getRubrics() : Observable<List<RubricData>>*/

}