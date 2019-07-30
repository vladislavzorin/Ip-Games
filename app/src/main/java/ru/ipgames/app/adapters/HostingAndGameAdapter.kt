package ru.ipgames.app.adapters
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_hosting.view.*
import ru.ipgames.app.R


class HostingAndGameAdapter: RecyclerView.Adapter<HostingAndGameAdapter.ViewHolder>() {
   private lateinit var listHostings:List<String>

    override fun onBindViewHolder(holder: HostingAndGameAdapter.ViewHolder, position: Int) {
        holder.bind(listHostings[position])
    }

    fun setList(list:List<String>){
        this.listHostings = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_hosting, parent, false)
        return HostingAndGameAdapter.ViewHolder(layout)
    }

    override fun getItemCount(): Int = if(::listHostings.isInitialized) listHostings.size else 0

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var textView = view.name

        fun bind(name:String){
            textView.text = name
        }
    }
}