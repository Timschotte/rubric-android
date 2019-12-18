package be.hogent.tile3.rubricapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentLoginBinding
import be.hogent.tile3.rubricapplication.security.AuthStateManager
import be.hogent.tile3.rubricapplication.security.Configuration
import be.hogent.tile3.rubricapplication.ui.LoginViewModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import net.openid.appauth.*

class LoginFragment : Fragment() {
    companion object {
        private const val FAILED = "failed"
        private const val RC_AUTH = 100
    }

    private lateinit var authStateManager: AuthStateManager
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    var authServerObserver = object : Observer<Boolean> {

        override fun onSubscribe(d: Disposable) {}
        override fun onNext(s: Boolean) {
            if (s) {
                startAuth()
            }
        }

        override fun onError(e: Throwable) {}
        override fun onComplete() {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authStateManager = AuthStateManager.getInstance(context!!)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginBtn.setOnClickListener { view: View ->
            loginViewModel.recreateAuthorizationService()
            startAuthProcess()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            Log.v("Auth", "login cancelled")
            // nothing
        } else {

            val response = AuthorizationResponse.fromIntent(data!!)
            val ex = AuthorizationException.fromIntent(data)
            when {
                response?.authorizationCode != null -> {
                    loginViewModel.authStateManager.updateAfterAuthorization(response, ex)
                    loginViewModel.exchangeAuthorizationCode(response)
                }
                ex != null -> return // Authorization flow failed
                else -> return // No authorization state retained - reauthorization required
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (authStateManager.current.isAuthorized) {
            val navController = this.findNavController()
            navController.currentDestination
            navController.navigate(R.id.action_loginFragment_to_mainMenuFragment)
        }
    }

    @MainThread
    fun startAuth() {
        loginViewModel.executor?.submit { doAuth() }
    }

    @WorkerThread
    private fun doAuth() {
        val intent =
            loginViewModel.authService?.getAuthorizationRequestIntent(loginViewModel.authRequest.get(), loginViewModel.authIntent.get())
        startActivityForResult(intent, RC_AUTH)
    }

    fun createAuthRequest() {
        val authRequestBuilder = AuthorizationRequest.Builder(
            loginViewModel.authStateManager.current.authorizationServiceConfiguration!!,
            loginViewModel.clientId.get(),
            ResponseTypeValues.CODE,
            loginViewModel.configuration.redirectUri!!
        ).setScope(loginViewModel.configuration.scope!!)

        loginViewModel.authRequest.set(authRequestBuilder.build())
        authServerObserver.onNext(true)
    }

    @WorkerThread
    fun initializeClient() {
        loginViewModel.clientId.set(loginViewModel.configuration.clientId)
        activity?.runOnUiThread { createAuthRequest() }
        return
    }

    fun startAuthProcess() {
        loginViewModel.configuration = Configuration.getInstance(requireContext())
        if (!loginViewModel.configuration.isValid) {
            return
        }
        loginViewModel.configuration.acceptConfiguration()
        loginViewModel.executor?.submit {
            initializeAppAuth()
        }
    }

    @WorkerThread
    fun initializeAppAuth() {
        val config = AuthorizationServiceConfiguration(
            loginViewModel.configuration.authEndpointUri!!,
            loginViewModel.configuration.tokenEndpointUri!!
        )
        loginViewModel.authStateManager.replace(AuthState(config))
        initializeClient()
        return
    }

}
