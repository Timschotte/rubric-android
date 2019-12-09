package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentMainMenuBinding

/**
 * MainMenu [Fragment] for showing the Main fragment
 * @see Fragment
 */
class MainMenuFragment : Fragment() {
    /**
     * Initializes the [MainMenuFragment] in CREATED state. Inflates the fragment layout, initializes databinding objects
     *  and onClickListeners handlers
     * @param inflater [LayoutInflater]
     * @param container [ViewGroup]
     * @param savedInstanceState [Bundle]
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**
         * Layout inflation
         */
        val binding: FragmentMainMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        /**
         * onClickListener
         */
        binding.startEvaluatieButton.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_opleidingSelectFragment)
        }
        /**
         * Other
         */
        return binding.root

    }



}
