package be.hogent.tile3.rubricapplication.model

import androidx.room.Embedded
import androidx.room.Relation

class EvaluatieRubric{
    @Embedded
    var rubric: Rubric? = null

    @Relation(parentColumn = "rubricId",
              entityColumn = "rubricId")
    var criteria: List<Criterium> = ArrayList()

    @Relation(parentColumn = "rubricId",
              entityColumn = "rubricId")
    var niveausCriteria: List<Niveau> = ArrayList()

    override fun toString() = "EvaluatieRubric voor rubric ${rubric?.onderwerp} (${rubric?.rubricId}) met ${criteria.size} criteria (eerste criterium: ${criteria[0].naam})"
}