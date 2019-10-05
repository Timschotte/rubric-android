package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.TestResource
import io.reactivex.Observable
import retrofit2.http.GET
interface TestApi {

    /**
     * Get a String from the backend
     */
    @GET("test")
    fun getTestString(): Observable<TestResource>
}