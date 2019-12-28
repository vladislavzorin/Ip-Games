package ru.ipgames.app.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.add_your_server_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.App
import ru.ipgames.app.R
import ru.ipgames.app.databinding.AddYourServerFragmentBinding
import ru.ipgames.app.utils.ViewAnimation
import ru.ipgames.app.viewModels.AddYourServerViewModel

class AddYourServerFragment : Fragment() {
    private lateinit var viewModelFragment: AddYourServerViewModel

    var viewList:ArrayList<View> = ArrayList()
    var titleList:ArrayList<View> = ArrayList()
    var stepList:ArrayList<RelativeLayout> = ArrayList()

    private var success_step = 0
    private var current_step = 0

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return DataBindingUtil.inflate<AddYourServerFragmentBinding>(
                                                                        inflater,
                                                                        R.layout.add_your_server_fragment,
                                                                        container,
                                                                        false
                                                                     ).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLists()
        initBinding()
    }

    private fun initLists(){
        viewList.add(list)
        viewList.add(ip)
        viewList.add(confirm)

        titleList.add(choose_game)
        titleList.add(edit_ip)
        titleList.add(add_server)

        stepList.add(step_title)
        stepList.add(step_ip)
        stepList.add(step_add)
    }

    private fun initBinding(){
        viewModelFragment  = ViewModelProviders.of(this).get(AddYourServerViewModel::class.java)
        DataBindingUtil.getBinding<AddYourServerFragmentBinding>(view!!)!!.run{
            initComponent(viewModelFragment)
            viewModel = viewModelFragment
            adView.loadAd(App.getAdRequest()) //реклама
        }
        initActionBar()
    }

    private fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "Добавить свой сервер"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
                                                        .maintoolbar
                                                        .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }


    private fun collapseAndContinue(number:Int){
        ViewAnimation.collapse(this.viewList[number])
        setCheckedStep(number)
        val nextNumber = number + 1
        current_step = nextNumber
        success_step = if (success_step < nextNumber) nextNumber else success_step
        ViewAnimation.expand(viewList[nextNumber])
    }

    private fun initComponent(viewModel: AddYourServerViewModel){
        val array = context!!.resources.getStringArray(R.array.gamelistID)
        viewModel.numberOfClick.observe(this, Observer{number ->
            /** первые 2 скрываем при нажатии на кнопку, на 3й передаем данные из первых двух **/
            if (number == 1) {viewModelFragment.ip = et_server_ip.text.toString()
                              viewModelFragment.gameId = array[gamespinner.selectedItemPosition]
                              hideKeyboard()
                             }

            collapseAndContinue(number?:0)
        })

       viewModel.getResultBooleanAdd.observe(this,Observer{result -> if (result!! )setCheckedStep(2) else setUnCheckedStep2()})

        viewModel.idOfTitleClick.observe(this, Observer{id ->
            when(id){
                R.id.choose_game->{
                    if (success_step >= 0 && current_step != 0) {
                        current_step = 0
                        collapseAll()
                        ViewAnimation.expand(viewList[0])
                    }
                }

                R.id.edit_ip ->{
                    if (success_step >= 1 && current_step != 1) {
                        current_step = 1
                        collapseAll()
                        ViewAnimation.expand(viewList[1])
                    }
                }

                R.id.add_server->{

                }
            }
        })
    }

    private fun collapseAll(){
        for (v in viewList) {
            ViewAnimation.collapse(v)
        }
    }

    private fun hideKeyboard(){
        val view = this.view
        if (view != null) {
            val imm: InputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setCheckedStep(index:Int){
        val relative:RelativeLayout = stepList[index]
        relative.background.setColorFilter(resources.getColor(R.color.green_700), PorterDuff.Mode.SRC_IN)
        relative.removeAllViews()
        val img = ImageButton(relative.context)
        img.setImageResource(R.drawable.ic_done)
        img.setBackgroundColor(Color.TRANSPARENT)
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        relative.addView(img)
    }

    private fun setUnCheckedStep2(){
        val relative:RelativeLayout = stepList[2]
        relative.background.setColorFilter(resources.getColor(R.color.red_600), PorterDuff.Mode.SRC_IN)
        relative.removeAllViews()
        val img = ImageButton(relative.context)
        img.setImageResource(R.drawable.ic_close)
        img.setBackgroundColor(Color.TRANSPARENT)
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        relative.addView(img)
    }

}