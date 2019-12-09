package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListAdapter
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListListener
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import com.google.android.material.chip.Chip
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class CriteriumEvaluatieFragment
    : Fragment() {

    lateinit var binding: FragmentCriteriumEvaluatieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            be.hogent.tile3.rubricapplication.R.layout.fragment_criterium_evaluatie,
            container,
            false
        )

        this.parentFragment?.let { it ->
            val criteriumOverzichtViewModel = ViewModelProviders.of(it)
                .get(CriteriumOverzichtViewModel::class.java)

            binding.criteriumOverzichtViewModel = criteriumOverzichtViewModel
            binding.criterium = criteriumOverzichtViewModel.geselecteerdCriterium.value
            binding.student = criteriumOverzichtViewModel.student
            criteriumOverzichtViewModel.geselecteerdCriterium.observe(this, Observer{
                binding.criterium = criteriumOverzichtViewModel.geselecteerdCriterium.value
            })

            criteriumOverzichtViewModel.geselecteerdCriterium.observe(
                viewLifecycleOwner,
                Observer { sel ->
                    sel.let {
                        binding.criterium = it
                    }
                })

            criteriumOverzichtViewModel.geselecteerdCriteriumNiveau.value?.let {
                    geselecteerdNiveau ->
                geselecteerdNiveau?.let {
                    displayChipsOfSelectedNiveau(it)
                }
            }

            criteriumOverzichtViewModel.geselecteerdCriteriumNiveau.observe(viewLifecycleOwner,
                Observer { geselecteerdNiveau ->
                    geselecteerdNiveau?.let {
                        displayChipsOfSelectedNiveau(it)
                    }
                })


            val adapter =
                CriteriumEvaluatieListAdapter(CriteriumEvaluatieListListener { niveauId, position ->
                    criteriumOverzichtViewModel.onNiveauClicked(niveauId, position)
                })

            binding.criteriumNiveausRecycler.isNestedScrollingEnabled = false

            binding.criteriumNiveausRecycler.adapter = adapter

            criteriumOverzichtViewModel.criteriumNiveaus.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })

            criteriumOverzichtViewModel.positieGeselecteerdCriteriumNiveau.observe(
                viewLifecycleOwner,
                Observer {
                    it?.let {
                        adapter.stelPositieGeselecteerdNiveauIn(it)
                        adapter.notifyDataSetChanged()
                    }
                })


            binding.voegCommentaarToeFloatingActionButton.setOnClickListener {
                val oudeCommentaar =
                    criteriumOverzichtViewModel.criteriumEvaluatie.value?.commentaar ?: ""

                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle(R.string.criterium_evaluatie_commentaar_dialog_titel)

                val input = EditText(this.context!!)
                input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                input.setSingleLine(false)
                input.setText(oudeCommentaar)
                input.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.secondaryVeryDarkColor
                    )
                )
                builder.setView(input)

                builder.setPositiveButton(R.string.criterium_evaluatie_commentaar_dialog_bevestig)
                { _, _ ->
                    criteriumOverzichtViewModel.onCommentaarChanged(
                        input.text.toString()
                    )
                }
                builder.setNegativeButton(R.string.criterium_evaluatie_commentaar_dialog_annuleer)
                { dialog, _ -> dialog.cancel() }

                builder.show()
                input.requestFocus()
            }

            /*binding.toonCriteriumOmschrijvingImageButton.setOnClickListener {


                AlertDialog.Builder(this.context!!).setTitle(
                    criteriumOverzichtViewModel.geselecteerdCriterium.value?.naam
                        ?: getString(R.string.criterium_evaluatie_omschrijving_dialog_titel_default)
                ).setMessage(
                    criteriumOverzichtViewModel.geselecteerdCriterium.value?.omschrijving
                        ?: getString(R.string.criterium_evaluatie_omschrijving_dialog_omschrijving_default)
                )
                    .setPositiveButton(
                        R.string.criterium_evaluatie_omschrijving_dialog_bevestig,
                        null
                    )
                    .create()
                    .show()
            }*/

            criteriumOverzichtViewModel.positieGeselecteerdCriterium.observe(
                viewLifecycleOwner,
                Observer {
                    binding.upEdgeButton.visibility = if (it == 0) View.INVISIBLE else View.VISIBLE
                    binding.downEdgeButton.visibility =
                        if (it == criteriumOverzichtViewModel.positieLaatsteCriterium.value ?: 0)
                            View.INVISIBLE else View.VISIBLE
                })

            binding.upEdgeButton.setOnClickListener {
                criteriumOverzichtViewModel.onUpEdgeButtonClicked()
            }

            binding.downEdgeButton.setOnClickListener {
                criteriumOverzichtViewModel.onDownEdgeButtonClicked()
            }
            criteriumOverzichtViewModel.score.observe(this, Observer{
                binding.scoreTextView.text = it.toString()
            })
            criteriumOverzichtViewModel.totaalScore.observe(this, Observer{
                binding.totaalscoreTextView.text = it.toString()
            })
            criteriumOverzichtViewModel.criteriumEvaluatie.observe(this, Observer{
                if(!it.commentaar.isNullOrBlank()) {
                    binding.commentaarTextView.text = "Commentaar: " + it.commentaar
                }else{
                    binding.commentaarTextView.text = ""
                }
            })
        }

        return binding.root
    }

    private fun displayChipsOfSelectedNiveau(geselecteerdNiveau: Niveau){
        binding.chipHolder.removeAllViews()
        var checked = false
        for (i in geselecteerdNiveau.ondergrens..geselecteerdNiveau.bovengrens) {
            val chip = layoutInflater.inflate(
                R.layout.chip_item_evaluatie,
                null,
                false
            ) as Chip
            chip.text = i.toString()
            chip.setOnClickListener {
                binding.criteriumOverzichtViewModel?.onScoreChanged(
                    Integer.parseInt(chip.text.toString())
                )
            }
            binding.chipHolder.addView(chip)
            if (chip.text == (binding.criteriumOverzichtViewModel?.criteriumEvaluatie?.value?.score
                    ?: 0).toString() || !checked
            ) {
                chip.isChecked = true
                checked = true
            }
        }
        binding.chipHolder.visibility = View.VISIBLE
    }
}
