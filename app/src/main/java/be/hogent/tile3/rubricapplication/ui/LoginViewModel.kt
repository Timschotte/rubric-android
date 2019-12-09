package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.security.AuthConnectionBuilder
import net.openid.appauth.*
import be.hogent.tile3.rubricapplication.security.AuthStateManager
import be.hogent.tile3.rubricapplication.security.Configuration
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    var accessToken: String = ""

    @Inject
    lateinit var context: Context

    lateinit var mAuthStateManager: AuthStateManager
    lateinit var mConfiguration: Configuration

    val mClientId = AtomicReference<String>()
    var mAuthService: AuthorizationService? = null
    val mAuthRequest = AtomicReference<AuthorizationRequest>()
    val mAuthIntent = AtomicReference<CustomTabsIntent>()
    var mExecutor: ExecutorService? = null

    init {
        mExecutor = Executors.newSingleThreadExecutor()
        App.component.inject(this)
    }

    @MainThread
    public fun exchangeAuthorizationCode(authorizationResponse: AuthorizationResponse) {
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
    public fun performTokenRequest(
        request: TokenRequest,
        callback: AuthorizationService.TokenResponseCallback
    ) {
        val clientAuthentication: ClientAuthentication
        try {
            clientAuthentication = mAuthStateManager.current.clientAuthentication
        } catch (ex: ClientAuthentication.UnsupportedAuthenticationMethod) {
            // Token request cannot be made, client authentication for the token endpoint could not be constructed
            return
        }
        val finalRequest: TokenRequest = getFinalRequest(request)

        mAuthService!!.performTokenRequest(finalRequest, clientAuthentication, callback)
    }

    public fun getFinalRequest(request: TokenRequest): TokenRequest {
        val params = HashMap<String, String>()
        params["client_secret"] = context.getString(R.string.ApiSecret)
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
    public fun handleCodeExchangeResponse(
        tokenResponse: TokenResponse?,
        authException: AuthorizationException?
    ) {
        if (authException != null) {
            // Log the error
            return
        }
        accessToken = tokenResponse!!.accessToken!!
    }

    public fun recreateAuthorizationService() {
        if (mAuthService != null) {
            mAuthService!!.dispose()
        }
        mAuthService = createAuthorizationService()
        mAuthRequest.set(null)
        mAuthIntent.set(null)
    }

    public fun createAuthorizationService(): AuthorizationService {
        val builder = AppAuthConfiguration.Builder()
        builder.setConnectionBuilder(AuthConnectionBuilder.INSTANCE)
        return AuthorizationService(context, builder.build())
    }

}
