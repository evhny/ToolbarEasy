package com.example.toolbarlib.custom.util

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class CountDrawable(context: Context) : Drawable() {

    private val mBadgePaint: Paint
    private val mTextPaint: Paint
    private val mTxtRect = Rect()

    private var mCount = ""
    private var mWillDraw: Boolean = false

    init {
        val mTextSize = 20f// context.resources.getDimension(R.dimen.badge_count_text_size)

        mBadgePaint = Paint()
        mBadgePaint.color = Color.YELLOW
            //ContextCompat.getColor(context.applicationContext, R.color.badge_background)
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL

        mTextPaint = Paint()
        mTextPaint.color =  Color.RED//ContextCompat.getColor(context.applicationContext, R.color.colorPrimary)
        mTextPaint.typeface = Typeface.DEFAULT
        mTextPaint.textSize = mTextSize
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

        // Position the badge in the top-right quadrant of the icon.
        // Using Math.max rather than Math.min //

        val radius = Math.max(width, height) / 2 / 2
        var posX = width * 0.1f
        val posiY = height - radius
        if (mCount.length <= 2) {
            // Draw badge circle.
            canvas.drawCircle(posX, posiY, (radius + radius * 0.5).toInt().toFloat(), mBadgePaint)
        } else {
            canvas.drawCircle(posX, posiY, (radius + radius * 0.6).toInt().toFloat(), mBadgePaint)
        }
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length, mTxtRect)

        val textY = height - height * 0.1f
        posX = width * 0.08f
        if (mCount.length > 2)
            canvas.drawText("99+", posX, textY, mTextPaint)
        else
            canvas.drawText(mCount, posX, textY, mTextPaint)
    }

    //Sets the count (i.e notifications) to display.
    fun setCount(count: String) {
        mCount = count

        // Only draw a badge if there are notifications.
        mWillDraw = !count.equals("0", ignoreCase = true)
        invalidateSelf()
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
