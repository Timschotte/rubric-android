package be.hogent.tile3.rubricapplication.injection.module

import be.hogent.tile3.rubricapplication.network.ExampleRubricApi
import be.hogent.tile3.rubricapplication.network.TestApi
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

@Module
object NetworkModule {


    /**
     * Provides the Test Service implemenation
     * @param retrofit the retrofit object used to instantiate the service
     */
    @Provides
    internal fun provideTestApi(retrofit: Retrofit): TestApi {
        return retrofit.create(TestApi::class.java)
    }

    @Provides
    internal fun provideExampleRubricApi(retrofit: Retrofit): ExampleRubricApi {
        return retrofit.create(ExampleRubricApi::class.java)
    }


    /**
     * Return the TestResource object.
     */
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