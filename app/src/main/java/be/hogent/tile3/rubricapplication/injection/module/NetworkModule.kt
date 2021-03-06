package be.hogent.tile3.rubricapplication.injection.module

import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.utils.BASE_URL
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {


    /**
     * Provides the Test Service implemenation
     * @param retrofit the retrofit object used to instantiate the service
     */
    @Singleton
    @Provides
    internal fun provideRubricApi(retrofit: Retrofit): RubricApi {
        return retrofit.create(RubricApi::class.java)
    }


    /**
     * Return the TestResource object.
     */
    @Singleton
    @Provides
    internal fun provideRetrofitInterface(): Retrofit {



        //HTTP client without logging
        val okHttpClient = OkHttpClient.Builder()
            .build()


        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


}