package ru.ipgames.app.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.ipgames.app.App

import ru.ipgames.app.R
import ru.ipgames.app.utils.GITHUB_URL
import ru.ipgames.app.utils.LAST_UPDATE
import ru.ipgames.app.utils.VERSION
import ru.ipgames.app.utils.VK_AUTOR_URL
import java.text.SimpleDateFormat
import java.util.*

class AboutFragment : Fragment(), RewardedVideoAdListener {

    lateinit var  database: FirebaseDatabase
    lateinit var  myRef: DatabaseReference
    private val mAuth = FirebaseAuth.getInstance()!!
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    private var isBtnClick = false
    private var isReward = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
    }

    private fun mobAds(){
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance((activity as AppCompatActivity).baseContext)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadRewardedVideoAd()
    }

    private fun initFirebase(){
        if (!::myRef.isInitialized) {
            database = FirebaseDatabase.getInstance()
            myRef = database.getReference("/").child("bonus").child("${mAuth.currentUser?.uid}")
        }

        val mFirebaseAnalytics = FirebaseAnalytics.getInstance((activity as AppCompatActivity).baseContext)
        val params = Bundle()
        params.putString("openAbout","app")
        mFirebaseAnalytics.logEvent("serverInfo",params)
    }

    private fun loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-6501617133685717/3892230479",AdRequest.Builder().build())
        //mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",AdRequest.Builder().build())
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initButtons()
        initActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mobAds()
    }

    private fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "О приложении"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
            .maintoolbar
            .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams

        adView.loadAd(App.getAdRequest())
    }

    private fun initButtons(){
        version.text = "$VERSION"
        update.text = "$LAST_UPDATE"
        vk_layout.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$VK_AUTOR_URL")))}
        github_layout.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_URL")))}
        bonus_btn.setOnClickListener {
                                        if (bonus_edit.text.isNotEmpty()){
                                            visibleScrollBar()
                                            isBtnClick = true

                                            if (mRewardedVideoAd.isLoaded){
                                                mRewardedVideoAd.show()
                                                isBtnClick = false
                                            }
                                        } else{
                                            Toast.makeText(activity,"Пустое поле!",Toast.LENGTH_LONG).show()
                                        }

                                     }
    }

    private fun visibleScrollBar(){
        bonus_btn.visibility = View.GONE
        bonus_edit.visibility = View.GONE
        about_progress.visibility = View.VISIBLE
    }

    private fun visibleEndText(){
        about_progress.visibility = View.GONE
        end_text.visibility = View.VISIBLE
    }

    private fun visibleEditBonus(){
        bonus_btn.visibility = View.VISIBLE
        bonus_edit.visibility = View.VISIBLE
        about_progress.visibility = View.GONE
        end_text.visibility = View.GONE
    }


    override fun onRewarded(reward: RewardItem) {
        visibleEndText()
        myRef.child("${SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())}").setValue("${bonus_edit.text}")
        bonus_edit.text.clear()
        isReward = true
    }


    override fun onRewardedVideoAdClosed() {
        if (!isReward){
            loadRewardedVideoAd()
            visibleEditBonus()
        }
    }

    override fun onRewardedVideoAdLoaded() {
        if (isBtnClick) {
            mRewardedVideoAd.show()
            isBtnClick = false
        }
    }

    override fun onRewardedVideoAdLeftApplication() {}
    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {visibleEditBonus()}
    override fun onRewardedVideoAdOpened() {}
    override fun onRewardedVideoStarted() {}
    override fun onRewardedVideoCompleted() {}
}
