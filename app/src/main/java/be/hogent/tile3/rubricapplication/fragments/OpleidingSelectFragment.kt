package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.ui.OpleidingViewModel
import be.hogent.tile3.rubricapplication.databinding.FragmentOpleidingSelectBinding

/**
 * A simple [Fragment] subclass.
 */
class OpleidingSelectFragment : Fragment() {

    private lateinit var viewModel: OpleidingViewModel
    private lateinit var binding: FragmentOpleidingSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opleiding_select, container, false)

        Log.i("OpleidingViewModel", "Called ViewModel")
        viewModel = ViewModelProviders.of(this).get(OpleidingViewModel::class.java)

        binding.selecteerOpleidingButton.setOnClickListener{ view: View ->
            Navigation.findNavController(view).navigate(R.id.action_opleidingSelectFragment_to_rubricSelectFragment)
        }

        return binding.root

    }


}
