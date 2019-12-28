package ru.ipgames.app.activities

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.graphics.Paint
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_server_info.*
import ru.ipgames.app.R
import ru.ipgames.app.databinding.ActivityServerInfoBinding
import ru.ipgames.app.model.Stats
import ru.ipgames.app.viewModels.ServerInfoViewModel
import java.text.SimpleDateFormat

class ServerInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServerInfoBinding
    private lateinit var viewModel: ServerInfoViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var server_ip:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        server_ip = intent.getStringExtra("server_ip")

        initBinding()
        initToolbar()
        initRefresh()

        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val params = Bundle()
        params.putString("server",server_ip)
        mFirebaseAnalytics.logEvent("openServerInfo", params)
    }

    override fun onStart() {
        super.onStart()
        LoadInfoAboutServer()
    }

    fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_info)
        linearLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.listOnlinePlayers.layoutManager = linearLayoutManager
        viewModel = ViewModelProviders.of(this).get(ServerInfoViewModel::class.java)
        viewModel.context = this
        binding.viewModel = viewModel
        viewModel.Datastats.observe(this, Observer{
            value -> if (value?.size?:0 !=0 ) {
            graphSettings(value!!)
        }
        })

        binding.btCopyIp.setOnClickListener(viewModel.setOnClickBtnCopy)
    }

    private fun initRefresh(){

        binding.swipeRefreshLayout.setOnRefreshListener {
            LoadInfoAboutServer()
        }

        viewModel.isShowRefresh.observe(this,Observer{value->
            binding.swipeRefreshLayout.isRefreshing = value!!
        })

    }

    private fun LoadInfoAboutServer(){
        viewModel.loagInfoAboutServer(server_ip)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Информация о сервере"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.favorite ->{
                if (viewModel.isFavoriteServer.value != false){
                    item.icon = resources.getDrawable(R.drawable.ic_favorite_border)
                    viewModel.deleteFavoriteServer()
                }else{
                    item.icon = resources.getDrawable(R.drawable.ic_favorite)
                    viewModel.insertFavoriteServer()
                }
                item.icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun graphSettings(value:List<Stats>){

        val plotPoints = arrayOfNulls<DataPoint>(value.size)

        binding.graph.viewport.setMinX(value[0].time*1000.toDouble())
        for (i in 0 until value.size) {
            plotPoints[i]= DataPoint(value[i].time*1000.toDouble(), value[i].players.toDouble())
        }

        binding.graph.addSeries(LineGraphSeries<DataPoint>(plotPoints))
        binding.graph.viewport.setMaxX(value[value.size-1].time*1000.toDouble())
        binding.graph.viewport.isXAxisBoundsManual = true
        binding.graph.viewport.isYAxisBoundsManual = true
        binding.graph.viewport.isScalable = true
        binding.graph.viewport.isScrollable = true
        binding.graph.gridLabelRenderer.isHighlightZeroLines = false
        binding.graph.gridLabelRenderer.verticalLabelsAlign = Paint.Align.LEFT
        binding.graph.gridLabelRenderer.labelHorizontalHeight = 140
        binding.graph.gridLabelRenderer.textSize = 20.toFloat()
        binding.graph.gridLabelRenderer.setHorizontalLabelsAngle(120)
        binding.graph.gridLabelRenderer.reloadStyles()
        binding.graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this,SimpleDateFormat("dd.MM hh:mm a"))
        binding.graph.gridLabelRenderer.setHumanRounding(true)
        binding.graph.gridLabelRenderer.numHorizontalLabels = 6

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_server_info, menu)

        val x = menu.findItem(R.id.favorite)
        x.icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)

        viewModel.isFavoriteServer.observe(this, Observer {value ->
            x.icon = if (value) resources.getDrawable(R.drawable.ic_favorite) else resources.getDrawable(R.drawable.ic_favorite_border)
            x.icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        })
        return true
    }

}


