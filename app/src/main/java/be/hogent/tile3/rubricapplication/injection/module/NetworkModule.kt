package be.hogent.tile3.rubricapplication.injection.module

import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.utils.BASE_URL
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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

        //To debug Retrofit/OkHttp we can intercept the calls and log them.
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
            this.addInterceptor(OkHttpProfilerInterceptor())
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

}