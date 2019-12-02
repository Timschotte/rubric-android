package be.hogent.tile3.rubricapplication.network

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class RubricApiTest {

    var mockServer: MockWebServer = MockWebServer()

    private lateinit var rubricApi: RubricApi

    private val sampleRubric = "[ { 'id' : 1, 'status' : 'PUBLIEK', 'onderwerp' : 'Communicatie update 2', 'omschrijving' : 'Eindcompetenties voor Communiceren bij de opleiding psychosociale zorg', 'criteriumGroepen' : [ { 'id' : 1, 'criteria' : [ { 'id' : 1, 'naam' : 'Luisterhouding', 'omschrijving' : 'Non-verbaal, lichaastaal, ...', 'gewicht' : 1, 'volgnummer' : 0, 'criteriumNiveaus' : [ { 'id' : 1, 'omschrijving' : 'De student kijkt continu weg van de patiënt', 'ondergrens' : 0, 'bovengrens' : 2, 'niveau' : { 'id' : 1, 'naam' : 'zwak', 'volgnummer' : -2 } }, { 'id' : 2, 'omschrijving' : 'De luisterhouding is minder goed omdat de student één of meerdere van onderstaande doet: ', 'ondergrens' : 3, 'bovengrens' : 4, 'niveau' : { 'id' : 2, 'naam' : 'onvoldoende', 'volgnummer' : -1 } }, { 'id' : 3, 'omschrijving' : 'De student maakt af en toe oogcontact en maakt af en toe gebruik van aandachtgevend gedrag.', 'ondergrens' : 5, 'bovengrens' : 6, 'niveau' : { 'id' : 3, 'naam' : 'vaardig', 'volgnummer' : 0 } }, { 'id' : 4, 'omschrijving' : ' De student maakt gepast oogcontact, gebruikt aandachtgevend gedrag gedurende het ganse gesprek. ', 'ondergrens' : 7, 'bovengrens' : 10, 'niveau' : { 'id' : 4, 'naam' : 'deskundig', 'volgnummer' : 1 } } ] }, { 'id' : 2, 'naam' : 'Theorie', 'omschrijving' : 'Theoretische onderbouwing bij nabevraging', 'gewicht' : 1, 'volgnummer' : 1, 'criteriumNiveaus' : [ { 'id' : 5, 'omschrijving' : 'de student gebruikt begrippen nooit in de juiste context, het is duidelijk dat hij deze niet begrijpt.', 'ondergrens' : 0, 'bovengrens' : 2, 'niveau' : { 'id' : 1, 'naam' : 'zwak', 'volgnummer' : -2 } }, { 'id' : 6, 'omschrijving' : 'De student gebruikt de begrippen en termen niet altijd in de juiste context. De student kan niet altijd argumenteren', 'ondergrens' : 3, 'bovengrens' : 4, 'niveau' : { 'id' : 2, 'naam' : 'onvoldoende', 'volgnummer' : -1 } }, { 'id' : 7, 'omschrijving' : 'De student gebruikt de begrippen en termen op de juiste manier en in de juiste context. Het is duidelijk dat hij deze begrijpt.', 'ondergrens' : 5, 'bovengrens' : 10, 'niveau' : { 'id' : 3, 'naam' : 'vaardig', 'volgnummer' : 0 } } ] }, { 'id' : 3, 'naam' : 'Correctheid en volledigheid', 'omschrijving' : '', 'gewicht' : 1, 'volgnummer' : 2, 'criteriumNiveaus' : [ { 'id' : 8, 'omschrijving' : 'de student betrekt geen of één van de gevraagde begrippen/vaardigheden', 'ondergrens' : 0, 'bovengrens' : 4, 'niveau' : { 'id' : 2, 'naam' : 'onvoldoende', 'volgnummer' : -1 } }, { 'id' : 9, 'omschrijving' : 'de student betrekt in zijn bespreking bijna alle gevraagde begrippen/vaardigheden', 'ondergrens' : 5, 'bovengrens' : 6, 'niveau' : { 'id' : 3, 'naam' : 'vaardig', 'volgnummer' : 0 } }, { 'id' : 10, 'omschrijving' : 'De student betrekt in zijn bespreking alle gevraagde begrippen/vaardigheden. ', 'ondergrens' : 7, 'bovengrens' : 8, 'niveau' : { 'id' : 4, 'naam' : 'deskundig', 'volgnummer' : 1 } }, { 'id' : 11, 'omschrijving' : 'De student betrekt in zijn bespreking alle gevraagde begrippen/vaardigheden en verwijst spontaan ook naar andere gepaste begrippen/vaardigheden. ', 'ondergrens' : 9, 'bovengrens' : 10, 'niveau' : { 'id' : 5, 'naam' : 'volleerd', 'volgnummer' : 2 } } ] } ] } ], 'niveauSchaal' : { 'id' : 1, 'niveaus' : [ { 'id' : 1, 'naam' : 'zwak', 'volgnummer' : -2 }, { 'id' : 2, 'naam' : 'onvoldoende', 'volgnummer' : -1 }, { 'id' : 3, 'naam' : 'vaardig', 'volgnummer' : 0 }, { 'id' : 4, 'naam' : 'deskundig', 'volgnummer' : 1 }, { 'id' : 5, 'naam' : 'volleerd', 'volgnummer' : 2 } ] }, 'opleidingsOnderdeel' : { 'id' : 65, 'naam' : 'Psychosociale Zorg en Verpleegkundige communicatie 3', 'docenten' : [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 ], 'studenten' : [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68 ], 'rubrics' : [ 50, 51, 55, 6, 52, 56, 40, 37, 49, 3, 45, 46, 7, 43, 4, 53, 54, 2, 39, 41, 8, 5, 47, 48, 1, 42, 44 ] }, 'datumTijdCreatie' : '2019-11-16T17:02:54.538153', 'datumTijdLaatsteWijziging' : '2019-12-02T11:56:48.636813' }]"

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
            val rubs = rubricApi.getRubrics().await()
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