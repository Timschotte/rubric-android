package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
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
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID

/**
 * A simple [Fragment] subclass.
 */
class LeerlingSelectFragment : Fragment() {

    lateinit var binding: FragmentLeerlingSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_leerling_select, container, false)
        val args = LeerlingSelectFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = LeerlingSelectViewModelFactory(args.rubricId.toLong(), args.opleidingsOnderdeelId)
        val leerlingSelectViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LeerlingSelectViewModel::class.java)

        binding.leerlingSelectViewModel = leerlingSelectViewModel
        binding.lifecycleOwner = this

        val adapter = LeerlingListAdapter(LeerlingListener {
                student -> leerlingSelectViewModel.onStudentClicked(student)
        })
        binding.leerlingList.adapter = adapter

        leerlingSelectViewModel.navigateToRubricView.observe(this, Observer { leerling ->
            leerling?.let {
                this.findNavController().navigate(
                    LeerlingSelectFragmentDirections
                        .actionLeerlingSelectFragmentToCriteriumOverzichtFragment(leerling,
                            args.rubricId, TEMP_EVALUATIE_ID, args.opleidingsOnderdeelId)
                )
                leerlingSelectViewModel.onStudentNavigated()
            }
        })


       leerlingSelectViewModel.gefilterdeStudenten.observe(viewLifecycleOwner, Observer {
            it?.let{
                System.out.println(it)
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.searchbar, menu)
        val searchBarOpleiding = menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView

        val editText = searchBarOpleiding.findViewById(R.id.search_src_text) as EditText

        editText.addTextChangedListener(
            object : TextWatcher {
                val handler = Handler()

                override fun afterTextChanged(s: Editable?) {
                    var text = s?.toString()
                    var millis:Long
                    if(text==""){
                        millis=0
                    } else {
                        millis=600
                    }
                    handler.postDelayed({
                        binding.leerlingSelectViewModel?.filterChanged(text)
                    }, millis)

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    handler.removeCallbacksAndMessages(null)
                }
            }
        )

        super.onCreateOptionsMenu(menu, inflater)
    }
}
