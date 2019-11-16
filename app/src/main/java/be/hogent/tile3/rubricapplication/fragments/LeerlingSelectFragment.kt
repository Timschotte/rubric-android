package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.LeerlingListAdapter
import be.hogent.tile3.rubricapplication.adapters.LeerlingListener
import be.hogent.tile3.rubricapplication.databinding.FragmentLeerlingSelectBinding
import be.hogent.tile3.rubricapplication.ui.LeerlingSelectViewModel
import be.hogent.tile3.rubricapplication.ui.factories.LeerlingSelectViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class LeerlingSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentLeerlingSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_leerling_select, container, false)
        val args = LeerlingSelectFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = LeerlingSelectViewModelFactory(args.rubricId, args.opleidingsOnderdeelId)
        val leerlingSelectViewModel = ViewModelProviders.of(this, viewModelFactory).get(LeerlingSelectViewModel::class.java)

        binding.leerlingSelectViewModel = leerlingSelectViewModel
        binding.setLifecycleOwner(this)

        val adapter = LeerlingListAdapter(LeerlingListener {
                studentId -> leerlingSelectViewModel.onStudentClicked(studentId)
        })
        binding.leerlingList.adapter = adapter

        leerlingSelectViewModel.navigateToRubricView.observe(this, Observer { leerling ->
            leerling?.let {
                this.findNavController().navigate(
                    LeerlingSelectFragmentDirections.actionLeerlingSelectFragmentToCriteriumOverzichtFragment(leerling, args.rubricId)
                )
                leerlingSelectViewModel.onStudentNavigated()
            }
        })


       leerlingSelectViewModel.studenten.observe(viewLifecycleOwner, Observer {
            it?.let{
                System.out.println(it)
                adapter.submitList(it)
            }
        })

        return binding.root

    }


}
