package be.hogent.tile3.rubricapplication.fragments


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriaListListener
import be.hogent.tile3.rubricapplication.adapters.CriteriumOverzichtListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumOverzichtBinding
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import be.hogent.tile3.rubricapplication.ui.factories.CriteriumOverzichtViewModelFactory

/**
 * CriteriumOverzicht [Fragment] for showing Criterium list
 * @property alertDialog [AlertDialog]
 * @property criteriumEvaluationFragment [CriteriumEvaluatieFragment]
 * @property criteriumOverzichtViewModel [CriteriumOverzichtViewModel]
 * @see Fragment
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class CriteriumOverzichtFragment : Fragment() {

    /**
     * Properties
     */
    private var alertDialog: AlertDialog? = null
    private lateinit var criteriumEvaluationFragment: CriteriumEvaluatieFragment
    private lateinit var criteriumOverzichtViewModel: CriteriumOverzichtViewModel
    /**
     * Initializes the [CriteriumOverzichtFragment] in CREATED state. Inflates the fragment layout, initializes ViewModel
     * databinding objects, observes ViewModel livedata, RecyclerView setup and onClickListeners handlers
     * @param inflater [LayoutInflater]
     * @param container [ViewGroup]
     * @param savedInstanceState [Bundle]
     * @see CriteriumOverzichtViewModel
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**
         * Layout inflation
         */
        val binding = DataBindingUtil.inflate<FragmentCriteriumOverzichtBinding>(
            inflater,
            R.layout.fragment_criterium_overzicht,
            container,
            false
        )
        /**
         * DataBinding
         */
        binding.criteriumOverzichtFragmentWrapper.visibility = View.INVISIBLE
        val args = CriteriumOverzichtFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = CriteriumOverzichtViewModelFactory(args.rubricId.toLong(), args.student)
        criteriumOverzichtViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CriteriumOverzichtViewModel::class.java)
        binding.lifecycleOwner = this
        /**
         * RecyclerView setup
         */
        val adapter = CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId, positie ->
                criteriumOverzichtViewModel.onGeselecteerdCriteriumGewijzigd(criteriumId, positie)
            })
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                val toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                if (itemCount > toPosition)
                    binding.rubricCriteriaListRecycler.smoothScrollToPosition(
                        toPosition
                    )
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val toPosition = criteriumOverzichtViewModel.positieGeselecteerdCriterium.value ?: 0
                binding.rubricCriteriaListRecycler.scrollToPosition(toPosition)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                val toPosition = criteriumOverzichtViewModel?.positieGeselecteerdCriterium.value ?: 0
                binding.rubricCriteriaListRecycler.smoothScrollToPosition(toPosition)
            }
        })
        binding.rubricCriteriaListRecycler.adapter = adapter
        /**
         * ViewModel livedata observers
         */
        criteriumOverzichtViewModel?.persisterenVoltooid.observe(viewLifecycleOwner, Observer {
            saved: Boolean ->
            run {
                if (saved) {
                    navigeerNaarLeerlingSelect()
                    criteriumOverzichtViewModel.navigatieNaPersisterenVoltooidCompleted()
                }

            }
        })
        criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.stelPositieGeselecteerdCriteriumIn(it)
                    adapter.notifyDataSetChanged()
                }
            })
        criteriumOverzichtViewModel?.evaluatieRubric?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.criteria)
                criteriumOverzichtViewModel.initialiseerEvaluatie()
                binding.criteriumOverzichtFragmentWrapper.visibility = View.VISIBLE
                binding.criteriumEvaluatieFragmentContainer.requestLayout()
            }
        })
        criteriumOverzichtViewModel.overzichtPaneelUitgeklapt.observe(viewLifecycleOwner, Observer { overzichtPaneelUitgeklapt: Boolean ->

            val displaymetrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
            val screenWidth = displaymetrics.widthPixels

            val animOverzichtBalk = ObjectAnimator.ofFloat(
                binding.criteriumEvaluatieOverzichtBalk,
                "translationX",
                binding.criteriumEvaluatieOverzichtBalk.translationX,
                if (!overzichtPaneelUitgeklapt)
                    binding.criteriumEvaluatieOverzichtBalk.translationX + resources
                        .getDimensionPixelOffset(R.dimen.criteria_overzicht_translationX)
                        .toFloat()
                else
                    0.0F
            )

            val animCriteriumEvaluatieFramePositie = ObjectAnimator.ofFloat(
                binding.criteriumEvaluatieFragmentWrapper,
                "translationX",
                binding.criteriumEvaluatieFragmentWrapper.translationX,
                if (!overzichtPaneelUitgeklapt)
                    binding.criteriumEvaluatieFragmentWrapper.translationX + resources
                        .getDimensionPixelOffset(R.dimen.criteria_overzicht_translationX)
                        .toFloat()
                else
                    0.0F
            )
            val tabletSize = resources.getBoolean(R.bool.isTablet)
            if (!tabletSize){
                animCriteriumEvaluatieFramePositie.doOnStart {
                    if(overzichtPaneelUitgeklapt){
                        binding.criteriumEvaluatieOverzichtBalk.visibility = View.VISIBLE

                    }else{
                        binding.criteriumEvaluatieFragmentContainer.visibility = View.VISIBLE
                    }
                }
                animCriteriumEvaluatieFramePositie.doOnEnd {
                    if(!overzichtPaneelUitgeklapt) {
                        binding.criteriumEvaluatieOverzichtBalk.visibility = View.INVISIBLE
                    }else{
                        binding.criteriumEvaluatieFragmentContainer.visibility = View.INVISIBLE
                    }
                }
            }

            val animCriteriumEvaluatieFrameBreedte = ValueAnimator.ofInt(
                binding.criteriumEvaluatieFragmentWrapper.measuredWidth,
                if (!overzichtPaneelUitgeklapt)
                    screenWidth - resources.getDimensionPixelOffset(R.dimen.criteria_overzicht_ingeklapt_breedte)
                else
                    screenWidth - resources.getDimensionPixelOffset(R.dimen.criteria_overzicht_width)
            )

            animCriteriumEvaluatieFrameBreedte.addUpdateListener { valueAnimator ->
                val animWaarde = valueAnimator.animatedValue as Int
                val layoutParams = binding.criteriumEvaluatieFragmentWrapper.layoutParams
                layoutParams.width = animWaarde
                binding.criteriumEvaluatieFragmentWrapper.layoutParams = layoutParams
            }


            val set = AnimatorSet()
            set.duration = 500L
            set.playTogether(
                animOverzichtBalk,
                animCriteriumEvaluatieFramePositie,
                animCriteriumEvaluatieFrameBreedte
            )
            set.start()

            if (overzichtPaneelUitgeklapt) {
                binding.rubricCriteriaLayout.visibility = View.VISIBLE
                binding.klapInKlapUitButton2.visibility = View.INVISIBLE
            } else {
                binding.rubricCriteriaLayout.visibility = View.INVISIBLE
                binding.klapInKlapUitButton2.visibility = View.VISIBLE
            }

            binding.criteriumEvaluatieFragmentContainer.requestLayout()

        })
        /**
         * onClickListeners
         */
        binding.klapInKlapUitButton.setOnClickListener {
            criteriumOverzichtViewModel.onKlapInKlapUitButtonClicked()
        }
        binding.klapInKlapUitButton2.setOnClickListener {
            criteriumOverzichtViewModel.onKlapInKlapUitButtonClicked()
        }
        /**
         * Other
         */
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Function used to created the options menu. Inflates the menu layout
     * @param menu [Menu]
     * @param inflater [MenuInflater]
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.criterium_evaluatie_menu, menu)
        menu.findItem(R.id.offline_state_icon).setVisible(false)
    }

    /**
     * Function used for handling user clicks in the options menu. Saving an Evaluatie or handling the Up button are handled here.
     * @param item [MenuItem]
     * @return [Boolean]
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_evaluatie_opslaan -> {
                persisteerEvaluatie()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Private function for persisting a current Evaluatie via ViewModel
     */
    private fun persisteerEvaluatie(){
        criteriumOverzichtViewModel.persisteerEvaluatie()
    }

    /**
     * Function that is called when the View is created. Sets up onKeyListener for navigate UP functionality
     * @param view [View]
     * @param savedInstanceState [Bundle]
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action === KeyEvent.ACTION_UP) {
                    onBackPressed()
                    return true
                }
                return false
            }
        })

        criteriumOverzichtViewModel.evaluatie.observe(this, Observer {
            it?.let {
                if (savedInstanceState == null) {
                    criteriumEvaluationFragment = CriteriumEvaluatieFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.criterium_evaluatie_fragment_container, criteriumEvaluationFragment)
                        .commitNow()
                }
            }
        })
    }

    /**
     * Private function handling navigate Up navigation. When in an evaluation, a confirmation dialop will be shown allowing the user
     * to save the evaluation or discard any changes made.
     */
    private fun onBackPressed() {
        val builder = AlertDialog.Builder(this.context!!)

        builder.setTitle(R.string.criterium_overzicht_back_dialog_titel)
        builder.setMessage(R.string.criterium_overzicht_back_dialog_body)
        builder.setPositiveButton(R.string.criterium_overzicht_back_dialog_opslaan) { _, _ ->
            persisteerEvaluatie()
        }
        builder.setNeutralButton(R.string.criterium_overzicht_back_dialog_terug) { dialog, _ ->
            dialog.cancel()
        }
        builder.setNegativeButton(R.string.criterium_overzicht_back_dialog_weggooien) { dialog, _ ->
            navigeerNaarLeerlingSelect()

        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    /**
     * Private function to navigate back to the [LeerlingSelectFragment]
     */
    private fun navigeerNaarLeerlingSelect(){
        criteriumOverzichtViewModel.deleteTempEvaluatie()
        val args = CriteriumOverzichtFragmentArgs.fromBundle(arguments!!)
        findNavController().navigate(
            CriteriumOverzichtFragmentDirections.actionCriteriumOverzichtFragmentToLeerlingSelectFragment(args.rubricId, args.olodId)
        )
    }

    /**
     * Function that is called when the Fragment is destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

}
