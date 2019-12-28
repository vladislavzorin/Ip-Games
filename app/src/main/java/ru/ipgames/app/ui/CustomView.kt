package ru.ipgames.app.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import ru.ipgames.app.R
import ru.ipgames.app.model.Players

class CustomView(context: Context,attr:AttributeSet) : View(context,attr){
    private val paint = Paint()
    private val rect= RectF()
    private var players: Players = Players(0,0)
    private var koff:Float=0.0F

    fun setPlayer(players:Players){
        this.players=players
        koff=players.now.toFloat()/players.max.toFloat()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(widthSize,widthSize)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.set(18.toFloat() ,18.toFloat() ,width.toFloat()-18,height.toFloat()-18)
        paint.isAntiAlias=true

        paint.color = when ((koff*100).toInt()){
            in 0..10 -> resources.getColor(R.color.red_600)
            in 11..20 -> resources.getColor(R.color.red_400)
            in 21..30 -> resources.getColor(R.color.orange_500)
            in 31..40 -> resources.getColor(R.color.amber_500)
            in 41..50 -> resources.getColor(R.color.yellow_600)
            in 51..60 -> resources.getColor(R.color.lime_500)
            in 61..70 -> resources.getColor(R.color.light_green_600)
            in 71..80 -> resources.getColor(R.color.green_400)
            in 81..90 -> resources.getColor(R.color.green_600)
            in 91..100 -> resources.getColor(R.color.green_700)
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

        paint.typeface = ResourcesCompat.getFont(context, R.font.open_sans_regular)

        paint.textSize=if ("${players.now}/${players.max}".length<=5) 34.toFloat() else
            if ("${players.now}/${players.max}".length<=7) 29.toFloat() else 22.toFloat()

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