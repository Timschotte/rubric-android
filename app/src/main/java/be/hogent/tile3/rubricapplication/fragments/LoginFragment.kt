package be.hogent.tile3.rubricapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentLoginBinding
import be.hogent.tile3.rubricapplication.ui.LoginViewModel

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import net.openid.appauth.*
import org.mayday.sociallogins.Configuration

class LoginFragment : Fragment() {
    companion object {
        private const val FAILED = "failed"
        private const val RC_AUTH = 100
    }

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
//            displayAuthCancelled()
            // nothing
        } else {

            val response = AuthorizationResponse.fromIntent(data!!)
            val ex = AuthorizationException.fromIntent(data)
            when {
                response?.authorizationCode != null -> {
                    loginViewModel.mAuthStateManager.updateAfterAuthorization(response, ex)
                    loginViewModel.exchangeAuthorizationCode(response)
                }
                ex != null -> return // Authorization flow failed
                else -> return // No authorization state retained - reauthorization required
            }
        }
    }

    @MainThread
    public fun startAuth() {
        loginViewModel.mExecutor?.submit { doAuth() }
    }

    @WorkerThread
    private fun doAuth() {
        val intent =
            loginViewModel.mAuthService?.getAuthorizationRequestIntent(loginViewModel.mAuthRequest.get(), loginViewModel.mAuthIntent.get())
        startActivityForResult(intent, RC_AUTH)
    }

    public fun createAuthRequest() {
        val authRequestBuilder = AuthorizationRequest.Builder(
            loginViewModel.mAuthStateManager.current.authorizationServiceConfiguration!!,
            loginViewModel.mClientId.get(),
            ResponseTypeValues.CODE,
            loginViewModel.mConfiguration.redirectUri!!
        ).setScope(loginViewModel.mConfiguration.scope!!)

        loginViewModel.mAuthRequest.set(authRequestBuilder.build())
        authServerObserver.onNext(true)
    }

    @WorkerThread
    public fun initializeClient() {
        loginViewModel.mClientId.set(loginViewModel.mConfiguration.clientId)
        activity?.runOnUiThread { createAuthRequest() }
        return
    }

    public fun startAuthProcess() {
        loginViewModel.mConfiguration = Configuration.getInstance(requireContext())
        if (!loginViewModel.mConfiguration.isValid) {
            return
        }
        loginViewModel.mConfiguration.acceptConfiguration()
        loginViewModel.mExecutor?.submit {
            initializeAppAuth()
        }
    }

    @WorkerThread
    public fun initializeAppAuth() {
        val config = AuthorizationServiceConfiguration(
            loginViewModel.mConfiguration.authEndpointUri!!,
            loginViewModel.mConfiguration.tokenEndpointUri!!
        )
        loginViewModel.mAuthStateManager.replace(AuthState(config))
        initializeClient()
        return
    }

}
