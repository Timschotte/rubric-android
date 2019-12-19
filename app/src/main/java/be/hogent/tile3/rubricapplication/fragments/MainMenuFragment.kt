package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentMainMenuBinding
import be.hogent.tile3.rubricapplication.security.AuthStateManager

/**
 * MainMenu [Fragment] for showing the Main fragment
 * @see Fragment
 */
class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var authStateManager: AuthStateManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        val navController = this.findNavController()
        binding.startEvaluatieButton.setOnClickListener { view: View ->
            navController.navigate(R.id.action_mainMenuFragment_to_opleidingSelectFragment)
        }

        authStateManager = AuthStateManager.getInstance(context!!)

        if (!authStateManager.current.isAuthorized) {
            navController.currentDestination
            navController.navigate(R.id.action_mainMenuFragment_to_loginFragment)
        } else{
            Log.v("Auth", "token:" + authStateManager.current.accessToken)
        }

        return binding.root
    }
}
