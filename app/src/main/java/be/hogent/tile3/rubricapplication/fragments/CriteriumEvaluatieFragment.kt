package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListAdapter
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListListener
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.ui.CriteriumEvaluatieViewModel
import be.hogent.tile3.rubricapplication.ui.NiveauViewModel

/**
 * A simple [Fragment] subclass.
 */
class CriteriumEvaluatieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCriteriumEvaluatieBinding>(
            inflater,
            R.layout.fragment_criterium_evaluatie,
            container,
            false
        )

        val criteriumEvaluatieViewModel = ViewModelProviders.of(this).get(
            CriteriumEvaluatieViewModel::class.java)

        binding.criteriumEvaluatieViewModel = criteriumEvaluatieViewModel

        criteriumEvaluatieViewModel.geselecteerdCriteriumNiveau.observe(viewLifecycleOwner, Observer{
                geselecteerdNiveau ->
            // NumberPicker minValue en maxValue niet mogelijk via databinding
            binding.scoreNumberPicker.minValue = geselecteerdNiveau.ondergrens
            binding.scoreNumberPicker.maxValue = geselecteerdNiveau.bovengrens

        })

        val adapter =
            CriteriumEvaluatieListAdapter(CriteriumEvaluatieListListener { niveauId ->
                criteriumEvaluatieViewModel.onNiveauClicked(niveauId)
            })

        binding.criteriumNiveausRecycler.adapter = adapter

        criteriumEvaluatieViewModel.criteriumNiveaus.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                it.let {
                    adapter.submitList(it)
                }
            }
        })

        return binding.root
    }


}
