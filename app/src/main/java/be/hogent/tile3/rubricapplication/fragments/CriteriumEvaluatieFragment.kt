package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListAdapter
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListListener
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumEvaluatieBinding
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CriteriumEvaluatieFragment
        : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCriteriumEvaluatieBinding>(
            inflater,
            R.layout.fragment_criterium_evaluatie,
            container,
            false
        )

        this.parentFragment?.let{
            val criteriumOverzichtViewModel
                    = ViewModelProviders.of(it)
                        .get(CriteriumOverzichtViewModel::class.java)

            binding.criteriumOverzichtViewModel = criteriumOverzichtViewModel
            binding.criterium = criteriumOverzichtViewModel.geselecteerdCriterium.value

            criteriumOverzichtViewModel.geselecteerdCriteriumNiveau.observe(viewLifecycleOwner, Observer{
                    geselecteerdNiveau ->
                // NumberPicker minValue en maxValue niet mogelijk via databinding
                geselecteerdNiveau?.let {
                    /*binding.scoreNumberPicker.minValue = geselecteerdNiveau.ondergrens
                    binding.scoreNumberPicker.maxValue = geselecteerdNiveau.bovengrens*/
                    binding.chipHolder.removeAllViews()
                    for (i in geselecteerdNiveau.ondergrens..geselecteerdNiveau.bovengrens){
                        var chip = Chip(context)
                        chip.text = i.toString()
                        chip.setOnClickListener { c -> criteriumOverzichtViewModel.onScoreChanged(0, Integer.parseInt(chip.text.toString())) }
                        binding.chipHolder.addView(chip)
                    }

                }

            })

            val adapter =
                CriteriumEvaluatieListAdapter(CriteriumEvaluatieListListener { niveauId, position ->
                    criteriumOverzichtViewModel.onNiveauClicked(niveauId, position)
                })

            binding.criteriumNiveausRecycler.isNestedScrollingEnabled = false

            binding.criteriumNiveausRecycler.adapter = adapter

            criteriumOverzichtViewModel.criteriumNiveaus.observe(viewLifecycleOwner, Observer{
                it?.let{
                    adapter.submitList(it)
                }
            })

            criteriumOverzichtViewModel.positieGeselecteerdCriteriumNiveau?.observe(viewLifecycleOwner, Observer{
                it?.let{
                    adapter.stelPositieGeselecteerdNiveauIn(it)
                    adapter.notifyDataSetChanged()
                }
            })

            criteriumOverzichtViewModel.criteriumEvaluatie?.observe(viewLifecycleOwner, Observer{
                it?.let{
                    /*binding.scoreNumberPicker.value = it.score ?: binding.scoreNumberPicker.minValue*/
                    //Werkte niet met numberpicker, ik laat dit momenteel zo
                }
            })

            binding.voegCommentaarToeFloatingActionButton.setOnClickListener {
                var oudeCommentaar =
                    criteriumOverzichtViewModel.criteriumEvaluatie.value?.commentaar ?: ""

                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle(R.string.criterium_evaluatie_commentaar_dialog_titel)

                val input = EditText(this.context!!)
                input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                input.setSingleLine(false)
                input.setText(oudeCommentaar)
                input.setTextColor(ContextCompat.getColor(context!!, R.color.secondaryTextColor))
                builder.setView(input)

                builder.setPositiveButton(R.string.criterium_evaluatie_commentaar_dialog_bevestig)
                { _, _ -> criteriumOverzichtViewModel.onCommentaarChanged(
                    oudeCommentaar,
                    input.text.toString()) }
                builder.setNegativeButton(R.string.criterium_evaluatie_commentaar_dialog_annuleer)
                { dialog, _ -> dialog.cancel() }

                builder.show()
            }

            binding.toonCriteriumOmschrijvingImageButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(criteriumOverzichtViewModel?.geselecteerdCriterium?.value?.naam
                        ?: getString(R.string.criterium_evaluatie_omschrijving_dialog_titel_default))
                    .setMessage(criteriumOverzichtViewModel?.geselecteerdCriterium?.value?.omschrijving
                        ?: getString(R.string.criterium_evaluatie_omschrijving_dialog_omschrijving_default))
                    .setPositiveButton(R.string.criterium_evaluatie_omschrijving_dialog_bevestig, null)
                    .show()
            }

            criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(viewLifecycleOwner, Observer{
                binding.upEdgeButton.visibility = if(it == 0) View.GONE else View.VISIBLE
                binding.downEdgeButton.visibility = if(it == criteriumOverzichtViewModel?.positieLaatsteCriterium?.value ?: 0) View.GONE else View.VISIBLE
            })

            binding.upEdgeButton.setOnClickListener{
                criteriumOverzichtViewModel?.onUpEdgeButtonClicked()
            }

            binding.downEdgeButton.setOnClickListener{
                criteriumOverzichtViewModel?.onDownEdgeButtonClicked()
            }
        }

        return binding.root
    }

}
