package be.hogent.tile3.rubricapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.LeerlingTextViewBinding
import be.hogent.tile3.rubricapplication.model.Student

class LeerlingListAdapter(val clickListener: LeerlingListener): ListAdapter<Student, LeerlingListAdapter.LeerlingViewHolder>(LeerlingDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeerlingViewHolder {
        Log.i("LeerlingLA", "Creating viewholder...")
        return LeerlingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LeerlingViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class LeerlingViewHolder private constructor(val binding: LeerlingTextViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Student, clickListener: LeerlingListener) {
            binding.student = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LeerlingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LeerlingTextViewBinding.inflate(layoutInflater, parent, false)
                return LeerlingViewHolder(binding)
            }
        }
    }
}


class LeerlingDiffCallback: DiffUtil.ItemCallback<Student>() {
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.studentId == newItem.studentId
    }

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem == newItem
    }
}

class LeerlingListener(val clickListener: (student: Student) -> Unit){
    fun onClick(student: Student) = clickListener(student)
}
