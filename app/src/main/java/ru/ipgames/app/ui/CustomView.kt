package ru.ipgames.app.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

import android.util.AttributeSet
import android.util.Log
import android.view.View
import ru.ipgames.app.R
import ru.ipgames.app.model.Players

import kotlin.math.roundToInt

class CustomView(context: Context,attrs:AttributeSet?) : View(context,attrs){
    private val paint= Paint()
    private val rect= RectF()
    var players: Players = Players(0,0)
    var koff:Float=0.0F
    private val defaultBarWidth = 50
    private val defaultBarHeight= 50

    fun setPlayer(players:Players){
        this.players=players
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> defaultBarWidth
            View.MeasureSpec.UNSPECIFIED -> defaultBarWidth
            else -> defaultBarWidth
        }

        val height = when (heightMode) {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> defaultBarHeight
            View.MeasureSpec.UNSPECIFIED -> defaultBarHeight
            else -> defaultBarHeight
        }
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.set(20.toFloat() ,20.toFloat() ,width.toFloat()-20,height.toFloat()-20)
        koff=players.now.toFloat()/players.max.toFloat()

        paint.isAntiAlias=true

        paint.color = when ((koff*100).toInt()){
            in 0..1-> resources.getColor(R.color.red_600)
            in 2..10 -> resources.getColor(R.color.red_400)
            in 11..20 -> resources.getColor(R.color.pink_600)
            in 21..30 -> resources.getColor(R.color.purple_600)
            in 31..40 -> resources.getColor(R.color.blue_200)
            in 41..50 -> resources.getColor(R.color.blue_600)
            in 51..60 -> resources.getColor(R.color.cyan_600)
            in 61..70 -> resources.getColor(R.color.teal_600)
            in 71..80 -> resources.getColor(R.color.light_green_600)
            in 81..90 -> resources.getColor(R.color.green_400)
            in 91..100 -> resources.getColor(R.color.green_600)
            else -> Color.GREEN
        }


        paint.style=Paint.Style.FILL
        canvas!!.drawCircle(width.toFloat() / 2.0F,height.toFloat() / 2.0F,height.toFloat() / 2.0F,paint)

        paint.style=Paint.Style.STROKE
        paint.strokeWidth=10.toFloat()
        paint.color= Color.GRAY
        canvas.drawArc(rect,135.toFloat(),270.toFloat(),false,paint)

        paint.color= Color.WHITE
        paint.style=Paint.Style.FILL
        paint.strokeWidth=1.toFloat()

        paint.textSize=if ("${players.now}/${players.max}".length<=5) 34.toFloat() else
            if ("${players.now}/${players.max}".length<=7) 30.toFloat() else 24.toFloat()

        canvas.drawText("${players.now}/${players.max}",
                (width.toFloat()-paint.measureText("${players.now}/${players.max}")) / 2.0F,
                height.toFloat() / 2.0F + paint.textSize / 2.0F ,
                paint)

        paint.color= Color.BLACK
        paint.style=Paint.Style.STROKE
        paint.strokeWidth=10.toFloat()
        canvas.drawArc(rect,135.toFloat(),(koff * 270),false,paint)

    }
}