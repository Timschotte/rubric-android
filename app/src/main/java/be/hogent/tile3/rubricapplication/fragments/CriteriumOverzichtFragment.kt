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
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumOverzichtBinding
import be.hogent.tile3.rubricapplication.injection.component.ViewModelInjectorComponent
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel

/**
 * A simple [Fragment] subclass.
 */
class CriteriumOverzichtFragment : Fragment() {

    private var criteriumOverzichtViewModel: CriteriumOverzichtViewModel? = null

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

        criteriumOverzichtViewModel =
            ViewModelProviders.of(this).get(CriteriumOverzichtViewModel::class.java)

        val adapter =
            CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId, positie ->
                Log.i("CriteriumOverzichtFrag","Geklikt op criterium met id " + criteriumId + "en positie " + positie)
                criteriumOverzichtViewModel?.onCriteriumClicked(criteriumId, positie)
            })

        binding.rubricCriteriaListRecycler.adapter = adapter

        criteriumOverzichtViewModel?.rubricCriteria?.observe(viewLifecycleOwner, Observer{
            Log.i("CriteriumOverzichtFrag", "New rubricCriteria list received, size: " + it?.size)
            it?.let{
                adapter.submitList(it)
            }
        })

        criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.stelPositieGeselecteerdCriteriumIn(it)
                adapter.notifyDataSetChanged()
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val criteriumEvaluatieFragment = CriteriumEvaluatieFragment(criteriumOverzichtViewModel)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.criterium_evaluatie_fragment_container, criteriumEvaluatieFragment).commit()
    }



}
