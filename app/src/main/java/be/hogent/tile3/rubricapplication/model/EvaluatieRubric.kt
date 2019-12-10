package be.hogent.tile3.rubricapplication.model

import androidx.room.Embedded
import androidx.room.Relation
/**
 * Model class for [EvaluatieRubric]
 * @constructor Creates a [EvaluatieRubric] class
 * @property rubric             ID ([Rubric]) that the [Evaluatie belongs to
 * @property criteria           List of [Criterium] that belongs to the [EvaluatieRubric]
 * @property niveausCriteria    List of [Niveau] that belongs to the [EvaluatieRubric]
 */
class EvaluatieRubric{
    @Embedded
    var rubric: Rubric? = null
    @Relation(parentColumn = "rubricId",
              entityColumn = "rubricId")
    var criteria: List<Criterium> = ArrayList()
    @Relation(parentColumn = "rubricId",
              entityColumn = "rubricId")
    var niveausCriteria: List<Niveau> = ArrayList()
    /**
     * Function to give a String representation of the [EvaluatieRubric] object
     */
    override fun toString() = "EvaluatieRubric voor rubric ${rubric?.onderwerp} (${rubric?.rubricId}) met ${criteria.size} criteria (eerste criterium: ${criteria[0].naam})"
}