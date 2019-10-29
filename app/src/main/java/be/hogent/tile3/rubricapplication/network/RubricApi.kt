package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.data.RubricData
import be.hogent.tile3.rubricapplication.data.RubricsResource
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RubricApi{

    @GET("rubric/{id}")
    fun getRubric(@Path("id") id: Int): Observable<RubricData>

    @GET("rubric")
    fun getRubrics() : Observable<List<RubricData>>

}