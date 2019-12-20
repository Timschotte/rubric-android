package be.hogent.tile3.rubricapplication.network

import androidx.activity.viewModels
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.findNavController
import androidx.test.espresso.action.ViewActions
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.activities.MainActivity
import be.hogent.tile3.rubricapplication.security.AuthStateManager
import be.hogent.tile3.rubricapplication.security.Configuration
import be.hogent.tile3.rubricapplication.ui.LoginViewModel
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference


class RubricApiTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    var mockServer: MockWebServer = MockWebServer()

    private lateinit var rubricApi: RubricApi

    @Before
    fun prep() {
        mockServer.start()
    }

    fun createApi() : RubricApi {

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
            this.addInterceptor(OkHttpProfilerInterceptor())
        }.build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(RubricApi::class.java)
    }


    @After
    fun breakdown() {
        mockServer.close()
    }

    @Test
    fun builder_CreatesApiInstance() {
        rubricApi = createApi()

        assert(rubricApi != null)
    }

    fun getJson(path: String): String {
        // Load the JSON response
        val fileContent = this::class.java.getResource(path).readText()
        return fileContent
    }

    @Test
    fun api_RubricIsRetrievedAndMapped() {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(getJson("/JSON/MockRubric.json"))
        mockedResponse.addHeader("Content-Type","application/json")
        mockedResponse.throttleBody(1024, 1, TimeUnit.SECONDS)
        mockServer.enqueue(mockedResponse)
        rubricApi = createApi()

        runBlocking {
            val rubs = rubricApi.getRubrics("All","").await()
            assert(rubs.isNotEmpty())
        }
    }

    @Test
    fun api_OlodIsRetrievedAndMapped() {

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(getJson("/JSON/MockOpleidingsOnderdelen.json"))
        mockedResponse.addHeader("Content-Type","application/json")
        mockedResponse.throttleBody(1024, 1, TimeUnit.SECONDS)
        mockServer.enqueue(mockedResponse)
        rubricApi = createApi()

        runBlocking {
            val olods = rubricApi.getOpleidingsOnderdeel().await()
            assert(olods.isNotEmpty())
        }
    }

    @Test
    fun api_StudentIsRetrievedAndMapped() {

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(getJson("/JSON/MockStudenten.json"))
        mockedResponse.addHeader("Content-Type","application/json")
        mockedResponse.throttleBody(1024, 1, TimeUnit.SECONDS)
        mockServer.enqueue(mockedResponse)
        rubricApi = createApi()

        runBlocking {
            val studenten = rubricApi.getStudenten().await()
            assert(studenten.isNotEmpty())
        }
    }

}