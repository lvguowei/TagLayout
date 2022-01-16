package com.example.taglayout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

class TagLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var adapter: TagsAdapter? = null
        set(value) {
            requireNotNull(value)
            field = value
            removeAllViews()
            field?.let {
                for (i in 0 until it.getCount()) {
                    val view = it.getView(i, this)
                    addView(view)
                }
            }
        }

    private val childrenBounds: MutableList<Rect> = mutableListOf()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthUsed = 0
        var heightUsed = 0
        var lineWidthUsed = 0
        var lineMaxHeight = 0
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            if (lineWidthUsed + child.measuredWidth + lp.marginStart + lp.marginEnd > specWidthSize && specWidthMode != MeasureSpec.UNSPECIFIED) {
                lineWidthUsed = 0
                heightUsed += lineMaxHeight
                lineMaxHeight = 0

                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }

            if (childrenBounds.size <= i) {
                childrenBounds.add(Rect())
            }
            val childBound = childrenBounds[i]
            childBound.set(
                lineWidthUsed + lp.marginStart,
                heightUsed + lp.topMargin,
                lineWidthUsed + lp.marginStart + child.measuredWidth,
                heightUsed + lp.topMargin + child.measuredHeight
            )
            lineWidthUsed += child.measuredWidth + lp.marginStart + lp.marginEnd
            widthUsed = max(widthUsed, lineWidthUsed)
            lineMaxHeight =
                max(lineMaxHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)
        }

        val width = widthUsed
        val height = heightUsed + lineMaxHeight
        setMeasuredDimension(width, height)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val bound = childrenBounds[i]
            child.layout(bound.left, bound.top, bound.right, bound.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}
