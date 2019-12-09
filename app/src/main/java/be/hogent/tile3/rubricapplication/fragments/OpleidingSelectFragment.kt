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
import be.hogent.tile3.rubricapplication.adapters.OpleidingsOnderdeelListAdapter
import be.hogent.tile3.rubricapplication.adapters.OpleidingsOnderdeelListener
import be.hogent.tile3.rubricapplication.ui.OpleidingsOnderdeelViewModel
import be.hogent.tile3.rubricapplication.databinding.FragmentOpleidingSelectBinding

/**
 * A simple [Fragment] subclass.
 */
class OpleidingSelectFragment : Fragment() {

    lateinit var binding:FragmentOpleidingSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opleiding_select, container, false)

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

        opleidingsOnderdeelViewModel.gefilterdeOpleidingsOnderdelen.observe(viewLifecycleOwner, Observer {
            it?.let{
                System.out.println(it)
                adapter.submitList(it)
            }
        })

        this.setHasOptionsMenu(true)


        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
                        binding.opleidingsOnderdeelViewModel?.filterChanged(text)
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
