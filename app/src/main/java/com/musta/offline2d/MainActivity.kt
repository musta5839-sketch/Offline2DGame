package com.musta.offline2d

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameView(this))
    }
}

private class GameView(context: Context) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val density = resources.displayMetrics.density
    private val radius = 42f * density
    private val targetRadius = 25f * density
    private val hitRadius = 35f * density

    private var playerX = 0f
    private var playerY = 0f
    private var targetX = 0f
    private var targetY = 0f
    private var score = 0
    private var initialized = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) return

        playerX = (w / 2f).coerceIn(radius, (w - radius).coerceAtLeast(radius))
        playerY = (h * 0.75f).coerceIn(radius, (h - radius).coerceAtLeast(radius))
        spawnTarget()
        initialized = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.rgb(16, 24, 39))

        paint.color = Color.rgb(56, 189, 248)
        canvas.drawCircle(playerX, playerY, radius, paint)

        paint.color = Color.rgb(251, 191, 36)
        canvas.drawCircle(targetX, targetY, targetRadius, paint)

        paint.color = Color.WHITE
        paint.textSize = 32f * density
        canvas.drawText("Score: $score", 24f * density, 48f * density, paint)

        paint.textSize = 20f * density
        canvas.drawText(
            "Tap the yellow target",
            24f * density,
            82f * density,
            paint
        )
    }

    private fun spawnTarget() {
        if (width <= 0 || height <= 0) return

        val margin = 50f * density
        val minX = margin.coerceAtMost(width / 2f)
        val maxX = (width - margin).coerceAtLeast(minX + 1f)
        val minY = (150f * density).coerceAtMost(height / 2f)
        val maxY = (height - 100f * density).coerceAtLeast(minY + 1f)

        targetX = Random.nextDouble(minX.toDouble(), maxX.toDouble()).toFloat()
        targetY = Random.nextDouble(minY.toDouble(), maxY.toDouble()).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked != MotionEvent.ACTION_DOWN || !initialized) {
            return true
        }

        val dx = event.x - targetX
        val dy = event.y - targetY

        if (dx * dx + dy * dy <= hitRadius * hitRadius) {
            score++
            spawnTarget()
        } else {
            if (width.toFloat() >= 2f * radius) {
                playerX = event.x.coerceIn(radius, width.toFloat() - radius)
            }
            if (height.toFloat() >= 2f * radius) {
                playerY = event.y.coerceIn(radius, height.toFloat() - radius)
            }
        }

        invalidate()
        return true
    }
}
