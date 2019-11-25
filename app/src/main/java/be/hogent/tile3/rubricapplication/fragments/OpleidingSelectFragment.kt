package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.OpleidingsOnderdeelListAdapter
import be.hogent.tile3.rubricapplication.adapters.OpleidingsOnderdeelListener
import be.hogent.tile3.rubricapplication.ui.OpleidingsOnderdeelViewModel
import be.hogent.tile3.rubricapplication.databinding.FragmentOpleidingSelectBinding

/**
 * A simple [Fragment] subclass.
 */
class OpleidingSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentOpleidingSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_opleiding_select, container, false)

        val opleidingsOnderdeelViewModel = ViewModelProviders.of(this).get(OpleidingsOnderdeelViewModel::class.java)

        binding.opleidingsOnderdeelViewModel = opleidingsOnderdeelViewModel
        binding.lifecycleOwner = this

        val adapter = OpleidingsOnderdeelListAdapter(OpleidingsOnderdeelListener { 
            opleidingsOnderdeelId -> opleidingsOnderdeelViewModel.onOpleidingsOnderdeelClicked(opleidingsOnderdeelId)
        }
            
        )
        binding.opleidingenList.adapter = adapter

        opleidingsOnderdeelViewModel.navigateToRubricSelect.observe(this, Observer { opleidingsOnderdeel ->
            opleidingsOnderdeel?.let {
                this.findNavController().navigate(
                    OpleidingSelectFragmentDirections.actionOpleidingSelectFragmentToRubricSelectFragment(opleidingsOnderdeel)
                )
                opleidingsOnderdeelViewModel.onOpleidingsOnderdeelNavigated()
            }
        })

        opleidingsOnderdeelViewModel.opleidingsOnderdelen.observe(viewLifecycleOwner, Observer {
            it?.let{
                System.out.println(it)
                adapter.submitList(it)
            }
        })

        return binding.root

    }


}
