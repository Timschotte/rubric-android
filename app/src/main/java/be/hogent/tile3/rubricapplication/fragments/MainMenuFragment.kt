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
 * A simple [Fragment] subclass.
 */
class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        binding.startEvaluatieButton.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_opleidingSelectFragment)
        }
        return binding.root

    }



}
