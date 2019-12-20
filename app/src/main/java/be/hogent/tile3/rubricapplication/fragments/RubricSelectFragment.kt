package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.RubricListener
import be.hogent.tile3.rubricapplication.adapters.RubricSelectListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentRubricSelectBinding
import be.hogent.tile3.rubricapplication.security.AuthStateManager
import be.hogent.tile3.rubricapplication.ui.RubricSelectViewModel
import be.hogent.tile3.rubricapplication.ui.factories.RubricSelectViewModelFactory

/**
 * RubricSelect [Fragment] for showing Rubric list
 * @property binding [FragmentRubricSelectBinding]
 * @see Fragment
 */
class RubricSelectFragment : Fragment() {
    /**
     * Properties
     */
    lateinit var binding: FragmentRubricSelectBinding

    /**
     * Initializes the [RubricSelectFragment] in CREATED state. Inflates the fragment layout, initializes ViewModel
     * databinding objects, observes ViewModel livedata and RecyclerView setup
     * @param inflater [LayoutInflater]
     * @param container [ViewGroup]
     * @param savedInstanceState [Bundle]
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Layout inflation
         */
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rubric_select, container, false)
        /**
         * ViewModel DataBinding
         */
        val opleidingsOnderdeel = RubricSelectFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory =
            RubricSelectViewModelFactory(opleidingsOnderdeel.opleidingsOnderdeelId)
        val rubricSelectViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RubricSelectViewModel::class.java)
        binding.rubricSelectViewModel = rubricSelectViewModel
        binding.lifecycleOwner = this
        /**
         * RecyclerView Setup
         */
        val adapter = RubricSelectListAdapter(RubricListener { rubricId ->
            rubricSelectViewModel.onRubricClicked(rubricId)
        })
        binding.rubricList.adapter = adapter
        /**
         * ViewModel livedata observers
         */

        val authStateManager = AuthStateManager.getInstance(context!!)
        val navController = this.findNavController()
        if (!authStateManager.current.isAuthorized) {
            navController.currentDestination
            navController.navigate(R.id.action_mainMenuFragment_to_loginFragment)
        }

        rubricSelectViewModel.navigateToKlasSelect.observe(this, Observer { rubric ->
            rubric?.let {
                this.findNavController().navigate(
                    RubricSelectFragmentDirections.actionRubricSelectFragmentToLeerlingSelectFragment(
                        rubric.toString(),
                        opleidingsOnderdeel.opleidingsOnderdeelId
                    )
                )
                rubricSelectViewModel.onOpleidingsOnderdeelNavigated()
            }
        })
        rubricSelectViewModel.gefilterdeRubrics.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        rubricSelectViewModel.refreshIsComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.spinningLoader.visibility = View.GONE
                binding.rubricList.visibility = View.VISIBLE
            } else {
                binding.spinningLoader.visibility = View.VISIBLE
                binding.rubricList.visibility = View.GONE
            }
        })
        /**
         * Other
         */
        this.setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Function used to created the options menu. Inflates the menu layout and add's a SearchBar
     * @param menu [Menu]
     * @param inflater [MenuInflater]
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.searchbar, menu)
        val searchBarRubric =
            menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView

        val editText = searchBarRubric.findViewById(R.id.search_src_text) as EditText


        editText.addTextChangedListener(
            object : TextWatcher {
                val handler = Handler()

                override fun afterTextChanged(s: Editable?) {
                    val text = s?.toString()
                    val millis: Long
                    if (text == "") {
                        millis = 0
                    } else {
                        millis = 600
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
