package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.LeerlingTextViewBinding
import be.hogent.tile3.rubricapplication.model.Student

/**
 * [RecyclerView] adapter for a [Student]-list
 * @param clickListener [LeerlingListener] for handling user clicks in [RecyclerView]
 * @see LeerlingListAdapter.LeerlingViewHolder
 * @see LeerlingDiffCallback
 */
class LeerlingListAdapter(val clickListener: LeerlingListener): ListAdapter<Student, LeerlingListAdapter.LeerlingViewHolder>(LeerlingDiffCallback()){
    /**
     * Function for creating a custom [LeerlingViewHolder]
     * @param parent [ViewGroup]
     * @param viewType [Int]
     * @return [LeerlingViewHolder] object
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeerlingViewHolder {
        return LeerlingViewHolder.from(parent)
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     * @param holder [LeerlingViewHolder]
     * @param position [Int]
     */
    override fun onBindViewHolder(holder: LeerlingViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }
    /**
     * Custom [LeerlingViewHolder] class
     * @constructor creates the ViewHolder
     * @property binding [LeerlingTextViewBinding] Binding object
     * @see RecyclerView.ViewHolder
     */
    class LeerlingViewHolder private constructor(val binding: LeerlingTextViewBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the [item] ([Student]), including [clickListener] ([LeerlingListener]), to the [LeerlingViewHolder]
         * @param item [Student]
         * @param clickListener [LeerlingListener]
         */
        fun bind(item: Student, clickListener: LeerlingListener) {
            binding.student = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            /**
             * Inflates the ItemView layout into the [LeerlingViewHolder]
             * @param parent [ViewGroup]
             * @return [LeerlingViewHolder]
             */
            fun from(parent: ViewGroup): LeerlingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LeerlingTextViewBinding.inflate(layoutInflater, parent, false)
                return LeerlingViewHolder(binding)
            }
        }
    }
}

/**
 * The RelationsListDiffCallback provides 2 functions to verify, when the list of Relations in the
 * RelationsList is updated, which Relations are new and which Relations' contents have changed.
 * This allows the RecyclerView to be more efficient in handling updated Relationslists.
 * @see DiffUtil.ItemCallback
 */
class LeerlingDiffCallback: DiffUtil.ItemCallback<Student>() {
    /**
     * Function to verify if two [Student] objects are the same by comparing id's
     * @param oldItem [Student]
     * @param newItem [Student]
     * @return [Boolean]
     */
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.studentId == newItem.studentId
    }
    /**
     * Function to verify if contents of two [Student] objects are the same. This is used to verify if an item has changed.
     * @param oldItem [Student]
     * @param newItem [Student]
     * @return [Boolean]
     */
    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem == newItem
    }
}
/**
 * ClickListener class for handling user onClick event on a [Student]-item in the [RecyclerView]
 * @property clickListener clickListener that returns StudentId for a clicked [Student]
 */
class LeerlingListener(val clickListener: (student: Student) -> Unit){
    fun onClick(student: Student) = clickListener(student)
}
