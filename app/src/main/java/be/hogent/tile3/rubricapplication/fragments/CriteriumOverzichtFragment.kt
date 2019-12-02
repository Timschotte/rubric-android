package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriaListListener
import be.hogent.tile3.rubricapplication.adapters.CriteriumOverzichtListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumOverzichtBinding
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import be.hogent.tile3.rubricapplication.ui.factories.CriteriumOverzichtViewModelFactory
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_criterium_evaluatie.*


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class CriteriumOverzichtFragment : Fragment() {

    private var alertDialog: AlertDialog? = null
    private lateinit var rubricEvaluationFragment: CriteriumEvaluatieFragment
    private lateinit var criteriumOverzichtViewModel: CriteriumOverzichtViewModel

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

        val args = CriteriumOverzichtFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = CriteriumOverzichtViewModelFactory(args.rubricId, args.student)
        criteriumOverzichtViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CriteriumOverzichtViewModel::class.java)


        criteriumOverzichtViewModel.evaluatieRubric.observe(viewLifecycleOwner, Observer{
            it?.let{
                Log.i("Test4", it.toString())
                 it.niveausCriteria.forEach{Log.i("Test4", it.toString())}
            }
        })

        val adapter =
            CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId, positie ->
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
            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                val toPosition =
                    criteriumOverzichtViewModel.positieGeselecteerdCriterium.value ?: 0
                binding.rubricCriteriaListRecycler.scrollToPosition(toPosition)
            }

            override fun onItemRangeRemoved(
                positionStart: Int,
                itemCount: Int
            ) {
                val toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium.value ?: 0
                binding.rubricCriteriaListRecycler.smoothScrollToPosition(toPosition)
            }
        })

        binding.rubricCriteriaListRecycler.adapter = adapter

        criteriumOverzichtViewModel?.persisterenVoltooid.observe(viewLifecycleOwner, Observer {
            saved: Boolean ->
            run {
                if (saved) {
                    navigeerNaarLeerlingSelect()
                    criteriumOverzichtViewModel.navigatieNaPersisterenVoltooidCompleted()
                }

            }
        })

        criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    adapter.stelPositieGeselecteerdCriteriumIn(it)
                    adapter.notifyDataSetChanged()
                }
            })

        criteriumOverzichtViewModel?.evaluatieRubric?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.criteria)
            }
        })

        binding.klapInKlapUitButton.setOnClickListener {
            criteriumOverzichtViewModel.onKlapInKlapUitButtonClicked()
        }

        binding.klapInKlapUitButton2.setOnClickListener {
            criteriumOverzichtViewModel.onKlapInKlapUitButtonClicked()
        }

        criteriumOverzichtViewModel.overzichtPaneelUitgeklapt.observe(viewLifecycleOwner,
            Observer { overzichtPaneelUitgeklapt: Boolean ->

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

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.criterium_evaluatie_menu, menu)
    }

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

    private fun persisteerEvaluatie(){
        criteriumOverzichtViewModel.persisteerEvaluatie()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action === KeyEvent.ACTION_UP) {
                    onBackPressed()
//                    fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return true
                }
                return false
            }
        })

        criteriumOverzichtViewModel.evaluatie.observe(this, Observer {
            it?.let {
                if (savedInstanceState == null) {
                    rubricEvaluationFragment = CriteriumEvaluatieFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.criterium_evaluatie_fragment_container, rubricEvaluationFragment)
                        .commitNow()
                }
            }
        })
    }

    private fun onBackPressed() {
        val builder = AlertDialog.Builder(this.context!!)

        builder.setTitle(R.string.criterium_overzicht_back_dialog_titel)
        builder.setMessage(R.string.criterium_overzicht_back_dialog_body)
        builder.setPositiveButton(R.string.criterium_overzicht_back_dialog_opslaan) { _, _ ->
            persisteerEvaluatie()
//            fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        builder.setNeutralButton(R.string.criterium_overzicht_back_dialog_terug) { dialog, _ ->
            dialog.cancel()
        }
        builder.setNegativeButton(R.string.criterium_overzicht_back_dialog_weggooien) { dialog, _ ->
//            fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            navigeerNaarLeerlingSelect()

        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    private fun navigeerNaarLeerlingSelect(){
        criteriumOverzichtViewModel.deleteTempEvaluatie()
        val args = CriteriumOverzichtFragmentArgs.fromBundle(arguments!!)
        findNavController().navigate(
            CriteriumOverzichtFragmentDirections.actionCriteriumOverzichtFragmentToLeerlingSelectFragment(args.rubricId, args.olodId)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

}
