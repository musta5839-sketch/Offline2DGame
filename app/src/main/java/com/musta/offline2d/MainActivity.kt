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
    private val hitRadius = 70f * density

    private var playerX = 0f
    private var playerY = 0f
    private var targetX = 0f
    private var targetY = 0f
    private var score = 0
    private var initialized = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.rgb(16, 24, 39))

        if (!initialized && width > 0 && height > 0) {
            playerX = width / 2f
            playerY = height * 0.75f
            spawnTarget()
            initialized = true
        }

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
        val margin = (50f * density).toInt()
        val top = (150f * density).toInt()
        val bottom = (100f * density).toInt()

        val minX = margin.coerceAtMost((width / 2).coerceAtLeast(1))
        val maxX = (width - margin).coerceAtLeast(minX + 1)
        val minY = top.coerceAtMost((height / 2).coerceAtLeast(1))
        val maxY = (height - bottom).coerceAtLeast(minY + 1)

        targetX = Random.nextInt(minX, maxX).toFloat()
        targetY = Random.nextInt(minY, maxY).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && initialized) {
            val dx = event.x - targetX
            val dy = event.y - targetY

            if (dx * dx + dy * dy < hitRadius * hitRadius) {
                score++
                spawnTarget()
            } else {
                if (width >= 2f * radius) {
                    playerX = event.x.coerceIn(radius, width - radius)
                }
                if (height >= 2f * radius) {
                    playerY = event.y.coerceIn(radius, height - radius)
                }
            }

            invalidate()
        }
        return true
    }
}
