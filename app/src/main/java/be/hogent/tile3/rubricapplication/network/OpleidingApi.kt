package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.Opleiding
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = " http://35.210.250.75:8080/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface OpleidingApiService{
    @GET("opleidingsonderdeel")
    fun getProperties():Deferred<List<Opleiding>>
}

object OpleidingApi {
    val retrofitService: OpleidingApiService by lazy {
        retrofit.create(OpleidingApiService::class.java)
    }
}
