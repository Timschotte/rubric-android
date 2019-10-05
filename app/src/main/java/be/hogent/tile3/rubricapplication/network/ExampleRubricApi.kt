package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.RubricResource
import be.hogent.tile3.rubricapplication.model.TestResource
import io.reactivex.Observable
import retrofit2.http.GET
interface ExampleRubricApi {

    /**
     * Get a String from the backend
     */
    @GET("rubric/1")
    fun getTestString(): Observable<RubricResource>
}