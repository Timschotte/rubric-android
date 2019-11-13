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
import androidx.navigation.fragment.findNavController
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.RubricListener
import be.hogent.tile3.rubricapplication.adapters.RubricSelectListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentRubricSelectBinding
import be.hogent.tile3.rubricapplication.ui.RubricSelectViewModel
import be.hogent.tile3.rubricapplication.ui.factories.RubricSelectViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class RubricSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRubricSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rubric_select, container, false)
        val opleidingsOnderdeel = RubricSelectFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = RubricSelectViewModelFactory(opleidingsOnderdeel.opleidingsOnderdeelId)
        val rubricSelectViewModel = ViewModelProviders.of(this, viewModelFactory).get(RubricSelectViewModel::class.java)

        binding.rubricSelectViewModel = rubricSelectViewModel
        binding.setLifecycleOwner(this)

        val adapter = RubricSelectListAdapter(RubricListener {
                rubricId -> rubricSelectViewModel.onRubricClicked(rubricId)
        })
        binding.rubricList.adapter = adapter

        rubricSelectViewModel.navigateToKlasSelect.observe(this, Observer { rubric ->
            rubric?.let {
                this.findNavController().navigate(
                    RubricSelectFragmentDirections.actionRubricSelectFragmentToKlasSelectFragment(rubric)
                )
                rubricSelectViewModel.onOpleidingsOnderdeelNavigated()
            }
        })

        rubricSelectViewModel.rubrics.observe(viewLifecycleOwner, Observer {
            it?.let{
                System.out.println(it)
                adapter.submitList(it)
            }
        })

        return binding.root
    }



}
