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
import be.hogent.tile3.rubricapplication.adapters.CriteriaListListener
import be.hogent.tile3.rubricapplication.adapters.CriteriumOverzichtListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumOverzichtBinding
import be.hogent.tile3.rubricapplication.ui.CriteriumEvaluatieViewModel
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel

/**
 * A simple [Fragment] subclass.
 */
class CriteriumOverzichtFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCriteriumOverzichtBinding>(
            inflater,
            R.layout.fragment_criterium_overzicht,
            container,
            false
        )

        val criteriumOverzichtViewModel = ViewModelProviders.of(this).get(
            CriteriumOverzichtViewModel::class.java)

        val adapter =
            CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId ->
                Log.i("CriteriumOverzichtFrag","")
            })

        binding.rubricCriteriaListRecycler.adapter = adapter

        criteriumOverzichtViewModel.rubricCriteria.observe(viewLifecycleOwner, Observer{
            Log.i("CriteriumOverzichtFrag", "New rubricCriteria list received, size: " + it?.size)
            it?.let{
                adapter.submitList(it)
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

}
