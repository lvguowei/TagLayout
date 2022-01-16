package com.example.taglayout

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import kotlin.random.Random


class ColoredTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            0F, 0F,
            width.toFloat(),
            height.toFloat(), CORNER_RADIUS.toFloat(), CORNER_RADIUS.toFloat(), paint
        )
        super.onDraw(canvas)
    }

    companion object {
        private val COLORS = intArrayOf(
            Color.parseColor("#E91E63"),
            Color.parseColor("#673AB7"),
            Color.parseColor("#3F51B5"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#009688"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#795548")
        )
        private val TEXT_SIZES = intArrayOf(
            16, 22, 28
        )
        private val CORNER_RADIUS = dpToPixel(4).toInt()
        private val X_PADDING = dpToPixel(16).toInt()
        private val Y_PADDING = dpToPixel(16).toInt()
    }

    init {
        setTextColor(Color.WHITE)
        textSize = TEXT_SIZES[Random.nextInt(3)]
            .toFloat()
        paint.color = COLORS[Random.nextInt(COLORS.size)]
        setPadding(X_PADDING, Y_PADDING, X_PADDING, Y_PADDING)
    }
}

fun dpToPixel(dp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
    Resources.getSystem().displayMetrics
)
