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
import be.hogent.tile3.rubricapplication.factories.OpleidingViewModelFactory
import be.hogent.tile3.rubricapplication.persistence.RubricsDatabase

/**
 * A simple [Fragment] subclass.
 */
class OpleidingSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentOpleidingSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_opleiding_select, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = RubricsDatabase.getDatabase(application).opleidingDao()

        val viewModelFactory = OpleidingViewModelFactory(dataSource, application)

        val opleidingViewModel = ViewModelProviders.of(this,viewModelFactory).get(OpleidingViewModel::class.java)

        binding.opleidingViewModel = opleidingViewModel

        binding.setLifecycleOwner(this)


        binding.selecteerOpleidingButton.setOnClickListener{ view: View ->
            Navigation.findNavController(view).navigate(R.id.action_opleidingSelectFragment_to_rubricSelectFragment)
        }

        return binding.root

    }


}
