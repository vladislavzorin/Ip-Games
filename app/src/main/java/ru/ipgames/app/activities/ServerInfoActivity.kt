package ru.ipgames.app.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.jjoe64.graphview.GridLabelRenderer
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
    private lateinit var linearLayoutManager:LinearLayoutManager
    private lateinit var server_ip:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = intent
        server_ip = intent.getStringExtra("server_ip")

        initBinding()
        initToolbar()
        initRefresh()
    }

    override fun onStart() {
        super.onStart()
        LoadInfoAboutServer()
    }

    fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_info)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        }
        return super.onOptionsItemSelected(item)
    }

    fun graphSettings(value:List<Stats>){

        var plotPoints = arrayOfNulls<DataPoint>(value.size)

        binding.graph.viewport.setMinX(value[0].time*1000.toDouble())
        for (i in 0..value.size-1) {
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
       // binding.graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
        binding.graph.gridLabelRenderer.setHorizontalLabelsAngle(120)
        binding.graph.gridLabelRenderer.reloadStyles()

        binding.graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this,SimpleDateFormat("dd.MM hh:mm a"))
        binding.graph.gridLabelRenderer.setHumanRounding(true)
        binding.graph.gridLabelRenderer.numHorizontalLabels = 6

    }

}


