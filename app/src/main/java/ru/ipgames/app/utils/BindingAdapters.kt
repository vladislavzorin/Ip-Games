package ru.ipgames.app.utils

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import ru.ipgames.app.R
import ru.ipgames.app.model.Players
import ru.ipgames.app.ui.CustomView
import ru.ipgames.app.utils.extension.getParentActivity


@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View,  visibility: MutableLiveData<Int>?) {
    val parentActivity:AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value?:View.GONE})
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView,  text: MutableLiveData<String>?) {
    val parentActivity:AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> if (value?:"".compareTo("") == 0) view.text = "error"
        else view.text = value })
    }
}

@BindingAdapter("mutableNow")
fun setMutableNow(view: CustomView, now: MutableLiveData<Players>?) {
    val parentActivity:AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && now != null) {
        now.observe(parentActivity, Observer { value -> view.setPlayer(value?:Players(0,0))})
    }
}

@BindingAdapter("mutableGameImage")
fun setMutableGameImage(view: ImageView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            Picasso.get()
                    .load("file:///android_asset/$value.png")
                    .resize(64, 64)
                    .error(R.drawable.ic_videogame_asset)
                    .into(view)
        })
    }
}

    @BindingAdapter("mutableMapImage")
    fun setMutableMapImage(view: ImageView, text: MutableLiveData<String>?) {
        val parentActivity: AppCompatActivity? = view.getParentActivity()
        if (parentActivity != null && text != null) {
            text.observe(parentActivity, Observer { value ->
                Picasso.get()
                        .load(value)
                        .error(R.drawable.ic_videogame_asset)
                        .into(view)
            })
        }
    }

@BindingAdapter("maxHeight")
fun setMaxHeight(view: LinearLayout, size:MutableLiveData<Int>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
        if (parentActivity != null) {
            size.observe(parentActivity, Observer { value ->
                var lp: ViewGroup.LayoutParams = view.layoutParams
                if (value != null) {
                    lp.height = if (value>5) 930 else ViewGroup.LayoutParams.WRAP_CONTENT
                }
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                view.layoutParams = lp
            })
        }
}


@BindingAdapter("colorCard")
fun setColorCard(view: CardView, position:MutableLiveData<Int>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null) {
        position.observe(parentActivity, Observer { value ->
            var array = view.context.resources.obtainTypedArray(R.array.mdcolor_random)
            view.setCardBackgroundColor(array.getColor(value!!.rem(14),1))
            array.recycle()
        })
    }
}



