package com.example.taglayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
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

  private val rows: MutableList<MutableList<View>> = mutableListOf()

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    // onMeasure may be called multiple times, so clear here
    rows.clear()

    val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
    var height = paddingTop + paddingBottom
    var lineWidth = paddingLeft + paddingRight

    var views = mutableListOf<View>()
    rows.add(views)

    var maxLineHeight = 0
    var maxLineWidth = lineWidth

    for (i in 0 until childCount) {
      val childView = getChildAt(i)
      if (childView.visibility == View.GONE) continue

      val layoutParams = childView.layoutParams as MarginLayoutParams

      measureChild(childView, widthMeasureSpec, heightMeasureSpec)

      if (lineWidth + childView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin > parentWidth) {
        height += maxLineHeight
        lineWidth =
          paddingLeft + childView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + paddingRight
        maxLineHeight = childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        views = mutableListOf()
        rows.add(views)
      } else {
        lineWidth += childView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
        maxLineHeight = max(
          maxLineHeight,
          childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        )
      }
      maxLineWidth = max(maxLineWidth, lineWidth)
      views.add(childView)
    }

    height += maxLineHeight

    setMeasuredDimension(
      View.resolveSize(maxLineWidth, widthMeasureSpec),
      View.resolveSize(height, heightMeasureSpec)
    )
  }


  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var left: Int
    var right: Int
    var top = 0
    var bottom: Int

    for (views in rows) {
      left = paddingLeft
      var maxHeight = 0
      for (view in views) {
        if (view.visibility == View.GONE) continue

        val layoutParams = view.layoutParams as MarginLayoutParams
        left += layoutParams.leftMargin
        val childTop = top + layoutParams.topMargin
        right = left + view.measuredWidth
        bottom = childTop + view.measuredHeight
        view.layout(left, childTop, right, bottom)

        left += view.measuredWidth + layoutParams.rightMargin

        val childHeight = view.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        maxHeight = max(childHeight, maxHeight)
      }
      top += maxHeight
    }
  }

  override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }
}