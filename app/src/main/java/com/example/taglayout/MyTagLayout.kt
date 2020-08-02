package com.example.taglayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import kotlin.math.max

class MyTagLayout @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

    Log.d("testtest", "Parent width = $parentWidth")

    var maxLineHeight = 0
    var resultHeight = 0
    var currentLineWidth = 0
    var maxLineWidth = 0

    for (n in 0 until childCount) {
      Log.d("testtest", "########### $n child #############")
      val child = getChildAt(n)
      measureChild(child, widthMeasureSpec, heightMeasureSpec)
      val params = child.layoutParams as MarginLayoutParams

      Log.d(
        "testtest",
        "measured width: ${child.measuredWidth}, measured height: ${child.measuredHeight}, margin_left: ${params.leftMargin}, margin_right: ${params.rightMargin}, margin_top: ${params.topMargin}, margin_bottom: ${params.bottomMargin}"
      )

      if (currentLineWidth + child.measuredWidth + params.rightMargin + params.leftMargin + paddingLeft + paddingRight > parentWidth) {
        resultHeight += maxLineHeight
        maxLineHeight = child.measuredHeight + params.bottomMargin + params.topMargin
        currentLineWidth = child.measuredWidth + params.rightMargin + params.leftMargin
        maxLineWidth = max(maxLineWidth, currentLineWidth)
      } else {
        maxLineHeight =
          max(maxLineHeight, child.measuredHeight + params.bottomMargin + params.topMargin)
        currentLineWidth += child.measuredWidth + params.rightMargin + params.leftMargin
        maxLineWidth = max(maxLineWidth, currentLineWidth)
      }
    }

    if (maxLineHeight != 0) {
      resultHeight += maxLineHeight
    }

    Log.d("testtest", "result height: $resultHeight")
    Log.d("testtest", "result width: $maxLineWidth")

    setMeasuredDimension(maxLineWidth, resultHeight + paddingTop + paddingBottom)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var left = l
    var top = t
    var maxLineHeight = 0

    for (n in 0 until childCount) {
      val child = getChildAt(n)
      val params = child.layoutParams as MarginLayoutParams

      if (left + params.leftMargin + child.measuredWidth + params.rightMargin > measuredWidth) {
        left = l
        top += maxLineHeight
        maxLineHeight = child.measuredHeight + params.topMargin + params.bottomMargin
      } else {
        maxLineHeight =
          max(maxLineHeight, child.measuredHeight + params.topMargin + params.bottomMargin)
      }
      val ll = left + params.leftMargin
      val tt = top + params.topMargin
      val rr = ll + child.measuredWidth + params.rightMargin
      val bb = tt + child.measuredHeight + params.bottomMargin
      child.layout(
        ll,
        tt,
        rr,
        bb
      )
      Log.d("testtest", "layout($ll, $tt, $rr, $bb)")
      left = rr
    }
  }

  override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
    return MarginLayoutParams(context, attrs)
  }

}