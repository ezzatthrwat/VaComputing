package me.ezzattharwat.vacomputing.ui.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.log_list_item.view.*
import me.ezzattharwat.vacomputing.R
import me.ezzattharwat.vacomputing.data.db.EquationEntity

class LogAdapter : RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    private val equationEntities =  ArrayList<EquationEntity>()

    fun setData(newEquations: List<EquationEntity>){
        equationEntities.clear()
        equationEntities.addAll(newEquations)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = LayoutInflater.from(parent.context).inflate(R.layout.log_list_item, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(equationEntities[position])
    }

    override fun getItemCount(): Int = equationEntities.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(equationItem: EquationEntity){

            val equation = "${equationItem.number1} ${equationItem.operator} ${equationItem.number2} = "
            itemView.equationTV.text = equation
            itemView.locationTV.text = equationItem.location
            if (equationItem.isCalculated){
                itemView.resultTV.text = "${equationItem.equationResult}"
            } else {
                itemView.resultTV.text = "Pending..."
            }
        }
    }

}