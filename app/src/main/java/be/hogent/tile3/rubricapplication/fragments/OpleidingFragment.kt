package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.ui.OpleidingViewModel
import be.hogent.tile3.rubricapplication.databinding.FragmentOpleidingBinding

/**
 * A simple [Fragment] subclass.
 */
class OpleidingFragment : Fragment() {

    private lateinit var viewModel: OpleidingViewModel
    private lateinit var binding: FragmentOpleidingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opleiding, container, false)

        Log.i("OpleidingViewModel", "Called ViewModel")
        viewModel = ViewModelProviders.of(this).get(OpleidingViewModel::class.java)

        return binding.root

    }


}
