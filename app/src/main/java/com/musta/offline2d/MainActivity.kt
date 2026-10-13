package com.musta.offline2d

import android.app.Activity
import android.os.Bundle
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameView())
    }
}

private class GameView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var playerX = 0f
    private var playerY = 0f
    private var targetX = 0f
    private var targetY = 0f
    private var score = 0
    private var radius = 42f

    constructor() : super(null)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.rgb(16, 24, 39))

        if (playerX == 0f) {
            playerX = width / 2f
            playerY = height * 0.75f
            spawnTarget()
        }

        paint.color = Color.rgb(56, 189, 248)
        canvas.drawCircle(playerX, playerY, radius, paint)

        paint.color = Color.rgb(251, 191, 36)
        canvas.drawCircle(targetX, targetY, 25f, paint)

        paint.color = Color.WHITE
        paint.textSize = 52f
        canvas.drawText("Score: $score", 36f, 76f, paint)

        paint.textSize = 30f
        canvas.drawText("Tap the yellow target", 36f, 124f, paint)
    }

    private fun spawnTarget() {
        targetX = Random.nextInt(50, (width - 50).coerceAtLeast(51)).toFloat()
        targetY = Random.nextInt(170, (height - 100).coerceAtLeast(171)).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val dx = event.x - targetX
            val dy = event.y - targetY
            if (dx * dx + dy * dy < 70f * 70f) {
                score++
                spawnTarget()
            } else {
                playerX = event.x.coerceIn(radius, width - radius)
                playerY = event.y.coerceIn(radius, height - radius)
            }
            invalidate()
            return true
        }
        return true
    }
}
