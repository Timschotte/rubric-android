package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.RubricFragment
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.ui.NiveauViewModel
import be.hogent.tile3.rubricapplication.utils.RubricUtils
import be.hogent.tile3.rubricapplication.views.NiveauView
import kotlinx.android.synthetic.main.criterium_list_content.view.*
import kotlinx.android.synthetic.main.niveau_cell.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class CriteriumRecyclerViewAdapter(private val parentFragment: RubricFragment) :
    ListAdapter<Criterium, CriteriumRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var niveauViewModel: NiveauViewModel
    //ListAdapter helps you to work with RecyclerViews that change the content over time.
    // All you need to do is submit a new list. It runs the DiffUtil tool behind the scenes for you on the background thread.
    // Then it runs the animations based on how the list has changed.
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Criterium>() {
            override fun areItemsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
                return false //TODO
            }

            override fun areContentsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
                return false //TODO
            }
        }
    }

    //click-listener for the items in the recyclerview
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            //the background colors of the cells
            val unselectedColor =  parentFragment.activity!!.applicationContext.resources.getColor(R.color.colorSecundaryLight)
            val selectedColor = parentFragment.activity!!.applicationContext.resources.getColor(R.color.colorSecundaryDark)

            //the cell we clicked on
            val item = v.tag as MultipleTagObject
            //we loop over the tableRow
            item.tableRow.children.iterator().forEach {
                if(it is NiveauView) {
                    if (it == v) {
                        //the cell we clicked on is the one we now iterate over, this cell should be selected
                        it.NiveauCellContainer.setCardBackgroundColor(selectedColor)
                    } else {
                        //this isn't the cell we clicked on, we reset the background color to the unselected state
                        it.NiveauCellContainer.setCardBackgroundColor(unselectedColor)
                    }
                }
            }
        }
    }

    //This will create the contentView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        niveauViewModel = ViewModelProviders.of(parentFragment.activity!!).get(NiveauViewModel::class.java)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.criterium_list_content, parent, false)
        return ViewHolder(view)
    }


    //This will fill the items with data and set a listener
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get the memory
        val criterium: Criterium = getItem(position)

        //fill the layout with data
        var criteriumNiveaus: List<Niveau>

        var rubricNiveaus: List<Niveau>
        doAsync {
            //All the cells that belong to the criterium
            criteriumNiveaus = niveauViewModel.getNiveausForCriterium(criterium.criteriumId )
            //All the cells that belong to the rubric
            rubricNiveaus = niveauViewModel.getNiveausForRubric(criterium.rubricId )
            //The first item in the tablerow is the title
            holder.description.text = criterium.naam

            onComplete {
                //we calculate the highest and lowest level so we know the max length of the rubric
                val hoogsteNiveau = RubricUtils.hoogsteNiveau(rubricNiveaus)
                val laagsteNiveau = RubricUtils.laagsteNiveau(rubricNiveaus)

                //we loop from the lowest to the highest level and fill the tableRow with views
                for(x in laagsteNiveau until hoogsteNiveau + 1){
                    //If there is a niveau for this level, create a view for it
                    if(criteriumNiveaus.any { y -> y.volgnummer == x }){
                        createNiveauView(criteriumNiveaus, x, criterium, holder)
                    } else{
                        //if there is no niveau for this level, we create an empty view so the niveaus with rank 0 will all be displayed beneath each other
                        createEmptyView(holder)
                    }
                }
            }

        }
    }

    private fun createEmptyView(holder: ViewHolder) {
        val whitespace = LinearLayout(parentFragment.activity!!)
        val layoutParam = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        layoutParam.setMargins(10, 10, 10, 10)
        whitespace.layoutParams = layoutParam
        holder.tableRow.addView(whitespace)
    }

    private fun createNiveauView(
        criteriumNiveaus: List<Niveau>,
        x: Int,
        criterium: Criterium,
        holder: ViewHolder
    ) {
        val huidigNiveau = criteriumNiveaus.filter { z -> z.volgnummer == x }.get(0)
        val niveauView = NiveauView(parentFragment.activity!!)
        val layoutParam = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        niveauView.clipToPadding = false

        niveauView.titel.text = huidigNiveau.titel
        niveauView.omschrijving.text = huidigNiveau.omschrijving
        if (x == 0) {
            initializeNiveauCell(niveauView, layoutParam)
        } else {
            initializeEmptyCell(layoutParam, niveauView)
        }
        niveauView.setTag(MultipleTagObject(huidigNiveau, criterium, holder.tableRow))
        niveauView.setOnClickListener(onClickListener)
        holder.tableRow.addView(niveauView)
    }

    private fun initializeEmptyCell(
        layoutParam: TableRow.LayoutParams,
        niveauView: NiveauView
    ) {
        layoutParam.setMargins(10, 10, 10, 10)
        niveauView.setPadding(5)
        niveauView.layoutParams = layoutParam
    }

    private fun initializeNiveauCell(
        niveauView: NiveauView,
        layoutParam: TableRow.LayoutParams
    ) {
        val resources = parentFragment.activity!!.applicationContext.resources
        val colorPrimary: Int = resources.getColor(R.color.colorPrimary)
        niveauView.NiveauCellContainer.strokeColor = colorPrimary
        niveauView.NiveauCellContainer.strokeWidth = 4
        niveauView.NiveauCellContainer.cardElevation = 40f
        niveauView.setPadding(5)
        layoutParam.setMargins(0, 0, 0, 0)
        niveauView.layoutParams = layoutParam
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.criteriumDescription
        val tableRow: TableRow = view.criterium_tablerow
    }

    inner class MultipleTagObject(val niveau: Niveau, val criterium: Criterium,
                                  val tableRow: TableRow
    )
}