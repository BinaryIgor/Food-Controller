package com.iprogrammerr.foodcontroller.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.MotionEvent
import com.iprogrammerr.foodcontroller.R

class CustomButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {

    private val white = Color.parseColor("#FFFFFF")
    private val black = Color.parseColor("#000000")
    private val colors: Map<String, Int>
    private val frameWidth: Int
    private val background: GradientDrawable
    private var down = false

    init {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0)
        val dpFromPx = { px: Float -> px * context.resources.displayMetrics.density }
        this.colors = mapOf(
            "color" to array.getColor(R.styleable.CustomButton_color, this.white),
            "pressedColor" to array.getColor(R.styleable.CustomButton_pressedColor, this.white),
            "textColor" to array.getColor(R.styleable.CustomButton_textColor, this.black),
            "pressedTextColor" to array.getColor(R.styleable.CustomButton_pressedTextColor, this.black),
            "frameColor" to array.getColor(R.styleable.CustomButton_frameColor, this.white),
            "pressedFrameColor" to array.getColor(R.styleable.CustomButton_pressedFrameColor, this.white)
        )
        this.frameWidth = dpFromPx(
            array.getDimension(
                R.styleable.CustomButton_frameWidth,
                0f
            )
        ).toInt()
        this.background = GradientDrawable()
        this.background.setColor(this.colors["color"] as Int)
        setTextColor(this.colors["textColor"] as Int)
        this.background.cornerRadii = cornerRadii(array, dpFromPx)
        this.background.setStroke(this.frameWidth, this.colors["frameColor"] as Int)
        setBackground(this.background)
        array.recycle()
    }

    private fun cornerRadii(array: TypedArray, dpFromPx: (Float) -> Float): FloatArray {
        val cornersRadii: FloatArray
        val cr = array.getDimension(R.styleable.CustomButton_cornersRadius, 0f)
        if (cr != 0f) {
            cornersRadii = FloatArray(8, {dpFromPx(cr) })
        } else {
            val topLeft = dpFromPx(
                array.getDimension(
                    R.styleable.CustomButton_topLeftCornerRadius, 0f
                )
            )
            val topRight = dpFromPx(
                array.getDimension(
                    R.styleable.CustomButton_topRightCornerRadius,
                    0f
                )
            )
            val bottomRight = dpFromPx(
                array.getDimension(
                    R.styleable.CustomButton_bottomRightCornerRadius,
                    0f
                )
            )
            val bottomLeft = dpFromPx(
                array.getDimension(
                    R.styleable.CustomButton_bottomLeftCornerRadius,
                    0f
                )
            )
            cornersRadii = floatArrayOf(
                topLeft, topLeft, topRight, topRight,
                bottomRight, bottomRight, bottomLeft, bottomLeft
            )
        }
        return cornersRadii
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (shouldBePressed(action)) {
            this.down = true
            press()
        } else if (shouldBeReleased(action)) {
            this.down = false
            release()
        }
        return super.onTouchEvent(event)
    }


    private fun shouldBePressed(action: Int): Boolean {
        return !this.down && action == MotionEvent.ACTION_DOWN
    }

    private fun shouldBeReleased(action: Int): Boolean {
        return this.down && (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_BUTTON_RELEASE ||
                action == MotionEvent.ACTION_CANCEL)
    }

    private fun press() {
        setTextColor(this.colors["pressedTextColor"] as Int)
        this.background.setColor(this.colors["pressedColor"] as Int)
        this.background.setStroke(this.frameWidth, this.colors["pressedFrameColor"] as Int)
    }

    private fun release() {
        setTextColor(this.colors["textColor"] as Int)
        this.background.setColor(this.colors["color"] as Int)
        this.background.setStroke(this.frameWidth, this.colors["frameColor"] as Int)
    }
}