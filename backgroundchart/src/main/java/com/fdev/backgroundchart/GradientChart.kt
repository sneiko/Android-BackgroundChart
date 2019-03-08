package com.fdev.backgroundchart

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import android.R.attr.path
import android.graphics.PathMeasure
import android.util.Log


class GradientChart : View {

    // region public settings
    var chartValues: Array<Float> = arrayOf(
        10f, 30f, 25f, 32f, 13f, 5f, 18f, 36f, 20f, 30f, 28f, 27f, 29f
    )
        set(value) {
            field = value
            invalidate()
        }

    // colors
    var chartLineColor: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    var plusColorStart: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    var plusColorEnd: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    var minusColorStart: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    var minusColorEnd: Int? = null
        set(value) {
            field = value
            invalidate()
        }

    // other
    var zoom: Int = 5
        set(value) {
            field = value
            invalidate()
        }

    var isBezier: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var bezierIntensity: Float = 0.5f
        set(value) {
            field = value
            invalidate()
        }
    // endregion

    // view settings
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var yCenter: Float = 0.0F
    private var xCenter: Float = 0.0F

    private var minChartValue = 0

    // paints
    private var clearPaint: Paint? = null
    private var chartLinePaint: Paint? = null
    private var plusPaint: Paint? = null

    // path
    private var chartPath: Path? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.GradientChart, defStyle, 0
        )

        chartLineColor = a.getColor(R.styleable.GradientChart_chartLineColor, Color.GREEN)
        plusColorStart = a.getColor(R.styleable.GradientChart_plusColorStart, Color.BLUE)
        plusColorEnd = a.getColor(R.styleable.GradientChart_plusColorEnd, Color.DKGRAY)
        minusColorStart = a.getColor(R.styleable.GradientChart_minusColorStart, Color.MAGENTA)
        minusColorEnd = a.getColor(R.styleable.GradientChart_minusColorEnd, Color.YELLOW)

        zoom = a.getInt(R.styleable.GradientChart_zoom, 1)
        isBezier = a.getBoolean(R.styleable.GradientChart_isBezier, true)
        bezierIntensity = a.getFloat(R.styleable.GradientChart_bezierIntensity, 0.5f)
    }

    private fun initPaints() {

        // chart line and minus rect
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            val gradient = LinearGradient(
                0f, mHeight.toFloat(), 0f, 0f, minusColorEnd!!, minusColorStart!!, Shader.TileMode.CLAMP
            )
            shader = gradient
            chartLinePaint = this
        }

        // upper rect
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            clearPaint = this
        }

        // upper rect
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            val gradient = LinearGradient(
                0f, mHeight.toFloat(), 0f, 0f, plusColorEnd!!, plusColorStart!!, Shader.TileMode.CLAMP
            )
            shader = gradient
            plusPaint = this
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)

        xCenter = (mWidth / 2).toFloat()
        yCenter = (mHeight / 2).toFloat()

        this.setMeasuredDimension(width, height)

        initPaints()
        setupChartPath()

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = w
        mHeight = h

        xCenter = (mWidth / 2).toFloat()
        yCenter = (mHeight / 2).toFloat()


        initPaints()
        setupChartPath()
    }

    private fun setupChartPath() {

        val chartPointStep = mWidth / chartValues.count()

        minChartValue = ((chartValues.max()!! * zoom) + yCenter).toInt()
        val maxChartValue = ((chartValues.min()!! * zoom) + yCenter).toInt()


        chartPath = Path().apply {
            var yChartCenter = yCenter - ((chartValues.max()!! * zoom) / 2)
            var xStart = 0f

            moveTo(xStart, mHeight.toFloat())
            lineTo(xStart, chartValues.first() + yCenter)

            var index = 0
            chartValues.forEach {
                xStart += chartPointStep

                if (isBezier) {

                    var prevDx = 0f
                    var prevDy = 0f
                    var curDx = 0f
                    var curDy = 0f

                    chartValues.let { array ->

                        var prevVal = 0f
                        if(index > 0) {
                            prevVal = array[index - 1]
                        } else {
                            prevVal = it
                        }

                        var nextVal = 0f
                        if(index < array.size - 1) {
                            nextVal = array[index + 1]
                        } else {
                            nextVal = it
                        }

                        prevDx = (xStart - (xStart - chartPointStep)) * bezierIntensity
                        prevDy = (it - prevVal) * bezierIntensity
                        curDx = ((xStart + chartPointStep) - xStart) * bezierIntensity // todo: 123
                        curDy = (nextVal - it) * bezierIntensity

                        this.cubicTo(
                            (xStart - chartPointStep) + prevDx, (((prevVal * zoom) + yChartCenter) + prevDy),
                            xStart - curDx, (((it * zoom) + yChartCenter) - curDy),
                            xStart, (it * zoom) + yChartCenter
                        )
                    }

                    index++
                } else {
                    this.lineTo(xStart, (it * zoom) + yCenter)
                }
            }

            lineTo(mWidth.toFloat(), mHeight.toFloat())
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        minusColorStart?.let { canvas.drawColor(it) }

        if (chartPath != null && chartLinePaint != null) {

            canvas.drawRect(0f, 0f, mWidth.toFloat(), minChartValue.toFloat(), plusPaint!!)
//            canvas.save()
            canvas.clipPath(chartPath!!, Region.Op.INTERSECT)
//            canvas.restore()
            canvas.drawPath(chartPath!!, chartLinePaint!!)
        }
    }
}
