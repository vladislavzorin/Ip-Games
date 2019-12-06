package ru.ipgames.app.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.toolbar.*

import ru.ipgames.app.R
import ru.ipgames.app.utils.GITHUB_URL
import ru.ipgames.app.utils.LAST_UPDATE
import ru.ipgames.app.utils.VERSION
import ru.ipgames.app.utils.VK_AUTOR_URL
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AboutFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

   lateinit var  database: FirebaseDatabase
   lateinit var  myRef: DatabaseReference
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (!::myRef.isInitialized) {
            database = FirebaseDatabase.getInstance()
            //database.setPersistenceEnabled(true)
            myRef = database.getReference("/").child("bonus").child("${mAuth.currentUser?.uid}")
        }

        val mFirebaseAnalytics = FirebaseAnalytics.getInstance((activity as AppCompatActivity).baseContext)
        val params = Bundle()
        params.putString("openAbout","app")
        mFirebaseAnalytics.logEvent("serverInfo",params)
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

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initActionBar(){
        (activity as AppCompatActivity).supportActionBar!!.title = "О приложении"

        val layoutParams: AppBarLayout.LayoutParams = (activity as AppCompatActivity)
            .maintoolbar
            .layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        (activity as AppCompatActivity).maintoolbar.layoutParams = layoutParams
    }

    fun initButtons(){
        version.text = "$VERSION"
        update.text = "$LAST_UPDATE"
        vk_layout.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$VK_AUTOR_URL")))}
        github_layout.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$GITHUB_URL")))}
        bonus_btn.setOnClickListener {
                                        myRef.child("${SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())}").setValue("${bonus_edit.text}")
                                        bonus_edit.text.clear()
                                        Toast.makeText(activity,"Бонус получен!",Toast.LENGTH_LONG).show()
                                     }
    }
}
