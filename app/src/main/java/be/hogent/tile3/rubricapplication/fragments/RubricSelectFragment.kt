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
import be.hogent.tile3.rubricapplication.adapters.RubricListener
import be.hogent.tile3.rubricapplication.adapters.RubricSelectListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentRubricSelectBinding
import be.hogent.tile3.rubricapplication.ui.RubricSelectViewModel
import be.hogent.tile3.rubricapplication.ui.factories.RubricSelectViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class RubricSelectFragment : Fragment() {

    lateinit var binding:FragmentRubricSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rubric_select, container, false)
        val opleidingsOnderdeel = RubricSelectFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = RubricSelectViewModelFactory(opleidingsOnderdeel.opleidingsOnderdeelId)
        val rubricSelectViewModel = ViewModelProviders.of(this, viewModelFactory).get(RubricSelectViewModel::class.java)

        binding.rubricSelectViewModel = rubricSelectViewModel
        binding.lifecycleOwner = this

        val adapter = RubricSelectListAdapter(RubricListener {
                rubricId -> rubricSelectViewModel.onRubricClicked(rubricId)
        })
        binding.rubricList.adapter = adapter

        rubricSelectViewModel.navigateToKlasSelect.observe(this, Observer { rubric ->
            rubric?.let {
                this.findNavController().navigate(
                    RubricSelectFragmentDirections.actionRubricSelectFragmentToLeerlingSelectFragment(rubric.toString(), opleidingsOnderdeel.opleidingsOnderdeelId)
                )
                rubricSelectViewModel.onOpleidingsOnderdeelNavigated()
            }
        })

        rubricSelectViewModel.gefilterdeRubrics.observe(viewLifecycleOwner, Observer {
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
                        binding.rubricSelectViewModel?.filterChanged(text)
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
