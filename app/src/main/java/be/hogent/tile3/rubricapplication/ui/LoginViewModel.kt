package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.graphics.Color
import android.util.Base64
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.security.AuthConnectionBuilder
import be.hogent.tile3.rubricapplication.security.AuthStateManager
import be.hogent.tile3.rubricapplication.security.Configuration
import net.openid.appauth.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    var accessToken: String = ""

    @Inject
    lateinit var context: Context

    var authStateManager: AuthStateManager
    lateinit var configuration: Configuration

    val clientId = AtomicReference<String>()
    var authService: AuthorizationService? = null
    val authRequest = AtomicReference<AuthorizationRequest>()
    var authIntent = AtomicReference<CustomTabsIntent>()
    var executor: ExecutorService? = null

    private var _authorizationSuccess= MediatorLiveData<Boolean>()
    val AuthorizationSuccess: LiveData<Boolean>
        get() = _authorizationSuccess

    init {
        App.component.inject(this)
        authStateManager = AuthStateManager.getInstance(context)
        authService = createAuthorizationService()
        executor = Executors.newSingleThreadExecutor()
    }

    @MainThread
    fun exchangeAuthorizationCode(authorizationResponse: AuthorizationResponse) {
        performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            AuthorizationService.TokenResponseCallback { tokenResponse, authException ->
                this.handleCodeExchangeResponse(
                    tokenResponse,
                    authException
                )
            })
    }

    @MainThread
    fun performTokenRequest(
        request: TokenRequest,
        callback: AuthorizationService.TokenResponseCallback
    ) {
        val clientAuthentication: ClientAuthentication
        try {
            clientAuthentication = authStateManager.current.clientAuthentication
        } catch (ex: ClientAuthentication.UnsupportedAuthenticationMethod) {
            // Token request cannot be made, client authentication for the token endpoint could not be constructed
            return
        }
        val finalRequest: TokenRequest = getFinalRequest(request)

        authService!!.performTokenRequest(finalRequest, clientAuthentication, callback)
    }

    fun getFinalRequest(request: TokenRequest): TokenRequest {
        val params = HashMap<String, String>()
        params[String(Base64.decode(context.getString(R.string.checksum_param), Base64.DEFAULT), StandardCharsets.UTF_16)] =
            String(Base64.decode(context.getString(R.string.rubric_checksum), Base64.DEFAULT), StandardCharsets.UTF_16)
        return TokenRequest.Builder(request.configuration, request.clientId)
            .setGrantType(request.grantType)
            .setAuthorizationCode(request.authorizationCode)
            .setRedirectUri(request.redirectUri)
            .setCodeVerifier(request.codeVerifier)
            .setScope(request.scope)
            .setRefreshToken(request.refreshToken)
            .setAdditionalParameters(params)
            .build()
    }

    @WorkerThread
    fun handleCodeExchangeResponse(
        tokenResponse: TokenResponse?,
        authException: AuthorizationException?
    ) {
        if (authException != null) {
            Log.e("AUTH", "Auth failed: "+authException.toJsonString())
            setAuthorizationSuccess(false)
            return
        }else if(tokenResponse!= null){
            authStateManager.updateAfterTokenResponse(tokenResponse, authException)
            Log.v("AUTH", "Auth success: "+tokenResponse.jsonSerialize())
            setAuthorizationSuccess(true)
        }

    }

    private fun setAuthorizationSuccess(bool: Boolean){
        _authorizationSuccess.value = true
    }

    fun recreateAuthorizationService() {
        if (authService != null) {
            authService!!.dispose()
        }
        authService = createAuthorizationService()
        authRequest.set(null)
        authIntent.set(CustomTabsIntent.Builder().setToolbarColor(Color.BLACK).build())
    }

    fun createAuthorizationService(): AuthorizationService {
        val builder = AppAuthConfiguration.Builder()
        builder.setConnectionBuilder(AuthConnectionBuilder.INSTANCE)
        return AuthorizationService(context, builder.build())
    }

}
