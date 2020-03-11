package com.example.toolbarlib.custom.util

import android.graphics.*
import android.graphics.drawable.Drawable

class CountDrawable(val textSize: Float) : Drawable() {

    private val mBadgePaint: Paint
    private val mTextPaint: Paint
    private val mTxtRect = Rect()

    private var mCount = ""
    private var mWillDraw: Boolean = false

    init {
        mBadgePaint = Paint()
        mBadgePaint.color = Color.YELLOW
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL

        mTextPaint = Paint()
        mTextPaint.color = Color.RED
        mTextPaint.typeface = Typeface.DEFAULT
        mTextPaint.textSize = textSize
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {

        if (!mWillDraw) {
            return
        }
        val bounds = bounds
        val width = (bounds.right - bounds.left).toFloat()
        val height = (bounds.bottom - bounds.top).toFloat()

        val radius = Math.max(width, height) / 2
        var posX = width / 2
        val posiY = height / 2
        if (mCount.length <= 2) {
            // Draw badge circle.
            canvas.drawCircle(posX, posiY, (radius).toInt().toFloat(), mBadgePaint)
        } else {
            canvas.drawCircle(posX, posiY, (radius).toInt().toFloat(), mBadgePaint)
        }
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length, mTxtRect)

        var textY = (height - (height - textSize) / 2) * 0.9f
        posX = width * 0.53f
        if (mCount.length > 2) {
            mTextPaint.textSize = textSize * 0.6f
            textY = (height - (height - mTextPaint.textSize) / 2) * 0.9f
            canvas.drawText("99+", posX, textY, mTextPaint)
        } else {
            canvas.drawText(mCount, posX, textY, mTextPaint)
        }
    }

    //Sets the count (i.e notifications) to display.
    fun setCount(count: String) {
        mCount = count

        // Only draw a badge if there are notifications.
        mWillDraw = !count.equals("0", ignoreCase = true)
        invalidateSelf()
    }

    fun setTextColor(intTextColor: Int) {
        mTextPaint.color = intTextColor
    }

    fun setBadgeColor(intColor: Int) {
        mBadgePaint.color = intColor
    }


    override fun setAlpha(alpha: Int) {
        // do nothing
    }

    override fun setColorFilter(cf: ColorFilter?) {
        // do nothing
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}
