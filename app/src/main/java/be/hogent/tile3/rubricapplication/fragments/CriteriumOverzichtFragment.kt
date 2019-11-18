package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.util.Log
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
import android.view.*
import android.widget.Toast
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.ui.factories.CriteriumOverzichtViewModelFactory
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog


class CriteriumOverzichtFragment : Fragment() {

    private var alertDialog: AlertDialog? = null

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

        val viewModelFactory = CriteriumOverzichtViewModelFactory(args.rubricId, args.studentId, args.evaluatieId)
        val criteriumOverzichtViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CriteriumOverzichtViewModel::class.java)

        criteriumOverzichtViewModel.rubricCriteria.observe(viewLifecycleOwner, Observer {
            it?.let{
                if(it.isNotEmpty()){
                    criteriumOverzichtViewModel.initaliseerWanneerNodig()
                }
            }
        })



        Toast.makeText(
            context,
            "Student ID: " + args.studentId + " Rubric ID: " + args.rubricId,
            Toast.LENGTH_LONG
        ).show()

        val adapter =
            CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId, positie ->
                criteriumOverzichtViewModel?.onCriteriumClicked(criteriumId, positie)
            })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                var toPosition =
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
                var toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                binding.rubricCriteriaListRecycler.scrollToPosition(
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                )
            }

            override fun onItemRangeRemoved(
                positionStart: Int,
                itemCount: Int
            ) {
                var toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                binding.rubricCriteriaListRecycler.smoothScrollToPosition(toPosition)
            }
        })

        binding.rubricCriteriaListRecycler.adapter = adapter

        criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    adapter.stelPositieGeselecteerdCriteriumIn(it)
                    adapter.notifyDataSetChanged()
                }
            })

        criteriumOverzichtViewModel?.rubricCriteria?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
//                binding.rubricCriteriaListRecycler.smoothScrollToPosition(
//                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0)
            }
        })

        binding.klapInKlapUitButton.setOnClickListener {
            criteriumOverzichtViewModel?.onKlapInKlapUitButtonClicked()
        }

        criteriumOverzichtViewModel?.overzichtPaneelUitgeklapt?.observe(viewLifecycleOwner,
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
                    binding.criteriumEvaluatieFragmentContainer,
                    "translationX",
                    binding.criteriumEvaluatieFragmentContainer.translationX,
                    if (!overzichtPaneelUitgeklapt)
                        binding.criteriumEvaluatieFragmentContainer.translationX + resources
                            .getDimensionPixelOffset(R.dimen.criteria_overzicht_translationX)
                            .toFloat()
                    else
                        0.0F
                )

                val animCriteriumEvaluatieFrameBreedte = ValueAnimator.ofInt(
                    binding.criteriumEvaluatieFragmentContainer.measuredWidth,
                    if (!overzichtPaneelUitgeklapt)
                        screenWidth - resources.getDimensionPixelOffset(R.dimen.criteria_overzicht_ingeklapt_breedte)
                    else
                        screenWidth - resources.getDimensionPixelOffset(R.dimen.criteria_overzicht_width)
                )

                animCriteriumEvaluatieFrameBreedte.addUpdateListener { valueAnimator ->
                    val animWaarde = valueAnimator.animatedValue as Int
                    val layoutParams = binding.criteriumEvaluatieFragmentContainer.layoutParams
                    layoutParams.width = animWaarde
                    binding.criteriumEvaluatieFragmentContainer.layoutParams = layoutParams
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
                    (binding.klapInKlapUitButton as ImageButton).setImageResource(android.R.drawable.ic_media_previous)
                } else {
                    binding.rubricCriteriaLayout.visibility = View.INVISIBLE
                    (binding.klapInKlapUitButton as ImageButton).setImageResource(android.R.drawable.ic_media_next)
                }

                binding.criteriumEvaluatieFragmentContainer.requestLayout()

            })

        binding.setLifecycleOwner(this)

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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun persisteerEvaluatie(){
        val criteriumOverzichtViewModel
                = ViewModelProviders.of(this)
            .get(CriteriumOverzichtViewModel::class.java)
        criteriumOverzichtViewModel.persisteerEvaluatie()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() === KeyEvent.ACTION_UP) {
                    onBackPressed()
//                    fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return true
                }
                return false
            }
        })

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.criterium_evaluatie_fragment_container, CriteriumEvaluatieFragment())
                .commitNow()
        }
    }



    private fun onBackPressed() {
        var builder = AlertDialog.Builder(this.context!!)

        builder.setTitle(R.string.criterium_overzicht_back_dialog_titel)
        builder.setMessage(R.string.criterium_overzicht_back_dialog_body)
        builder.setPositiveButton(R.string.criterium_overzicht_back_dialog_opslaan) { _, _ ->
            persisteerEvaluatie()
            fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        builder.setNeutralButton(R.string.criterium_overzicht_back_dialog_terug) { dialog, _ ->
            dialog.cancel()
        }
        builder.setNegativeButton(R.string.criterium_overzicht_back_dialog_weggooien) { dialog, _ ->
            fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

}
