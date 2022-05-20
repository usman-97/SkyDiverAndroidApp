package com.usk.skydiver

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi

/**
 * GameObjectSurfaceView present the view of the game
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("UseCompatLoadingForDrawables")
class GameSurfaceView(context: Context?, attrs: AttributeSet) : SurfaceView(context, attrs), Runnable {
    var paint: Paint = Paint()
    var objectPaint: Paint = Paint()
    var containerPaint: Paint = Paint()

    var objPaint: Paint = Paint()
    var paintObject: ObjectPaint = ObjectPaint(objPaint) // Custom paint object

    // var isRunning: Boolean = true
    var myThread: Thread
    private var myHolder: SurfaceHolder

    var skyDiver: SkyDiver
    var clouds = mutableListOf<AnimatedObject>()
    var enemies = mutableListOf<Enemy>()
    private var pickableObjects = mutableListOf<PickableObject>()
    var ocean: AnimatedObject
    var submarine: Submarine
    var rocket: Rocket

    var skyDiverPosition: Float = 0.0f

    var skyDiverContainer: Rect
    // var enemiesContainer = mutableListOf<Rect>()

    lateinit var newtonLaws: NewtonLaws
    var changeInNetForce: Float = 0f

    // val totalDistance = 5000
    val totalDistance = 100000 // Total distance to cover
    var distanceCovered = 0 // Total distance covered so far
    var distance: Int = totalDistance // Distance left to cover

    var hours = 0 // time frame
    var timer: Timer
    lateinit var rocketCoolDownTimer: Timer
    var isEnded: Boolean = false // Check if game is ended or completed
    var isPaused: Boolean = false // Check if game is resumed or paused
    var changeInVelocity: Int = 0

    private var upArrowIcon: Icon
    private var downArrowIcon: Icon

    // var gameFragment: GameFragment = GameFragment.newInstance() as GameFragment

    init {

        // Background colour of the view
        paint.color = Color.parseColor("#B3E5FC")
        containerPaint.color = Color.parseColor("#03A9F4")
        objectPaint.color = Color.BLACK
        objectPaint.textSize = 50.0f

        // Set up cloud animated objects
        val cloudImage: Drawable = context!!.resources.getDrawable(R.drawable.cloud, null)
        clouds.add(AnimatedObject(250, 500, 0, 5, cloudImage, 300, 300))
        clouds.add(AnimatedObject(750, 1000, 0, 5, cloudImage, 300, 300))

        // Set up enemies objects
        val helicopterLImage: Drawable = context.resources.getDrawable(R.drawable.helicopter_l, null)
        val helicopterRImage: Drawable = context.resources.getDrawable(R.drawable.helicopter_r, null)
        enemies.add(Helicopter(0, 1500, 3, 5, helicopterLImage, 400, 400))

        enemies.add(Helicopter(550, 1500, -3, 5, helicopterRImage, 400, 400))
//        enemiesContainer.add(Rect(enemies[1].x, enemies[1].y, enemies[1].x + enemies[1].width, enemies[1].y + enemies[1].height))

        enemies[0].spawnOnRandomXPosition(0, 100)
        enemies[1].spawnOnRandomXPosition(750, 800)

        // Set up main player object
        val skyDiverImage: Drawable = context.resources.getDrawable(R.drawable.skydiver, null)
        skyDiver = SkyDiver(300, 550, 5, 0, skyDiverImage, 350, 350)
        skyDiverContainer = Rect(skyDiver.x, skyDiver.y, skyDiver.x + skyDiver.width, skyDiver.y + skyDiver.height)

        val rocketImage: Drawable = context.resources.getDrawable(R.drawable.rocket, null)
        rocket = Rocket(0, 0, 0, 6, rocketImage, 200, 150)

        val submarineImage: Drawable = context.resources.getDrawable(R.drawable.submarine, null)
        submarine = Submarine(200, 1600, 10, 5, submarineImage, 400, 450)

        val oceanImage: Drawable = context.resources.getDrawable(R.drawable.water, null)
        ocean = AnimatedObject(0, 1200, 0, 5, oceanImage, 1200, 1600)

        pickableObjects.add(PickableObject(0, 1500, 0, 6, rocketImage, 200, 200))

        val upArrowImage: Drawable = context.resources.getDrawable(R.drawable.arrow_upward, null)
        upArrowIcon = Icon(70, 70, upArrowImage)

        val downArrowImage: Drawable = context.resources.getDrawable(R.drawable.arrow_downward, null)
        downArrowIcon = Icon(70, 70, downArrowImage)

        timer = Timer(distance) // Set the timer
        rocketCoolDownTimer = Timer(rocket.coolDownTimerLimit) // Cool down timer for rocket

        newtonLaws = NewtonLaws(0, 0, 75)

        // choose this view class as active thread
        myThread = Thread(this)
        myThread.start()
        myHolder = holder
        // Log.d("test", "${enemies[0].x}")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // return super.onTouchEvent(event)
        // rocket.targetY = event!!.y.toInt()
        // Launch the rocket if player has collectd any of it
        if (skyDiver.pickableObjects.isNotEmpty())
        {
            if (!rocket.isLaunched)
                skyDiver.pickableObjects.removeLast()
            rocket.isLaunched = true
        }
        return true
    }

    override fun run() {
        // If game is still not over or paused
        while (!isEnded && !isPaused)
        {
            if (!myHolder.surface.isValid)
                continue

            // Main canvas
            val canvas:Canvas = myHolder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

            timer.startTimer() // start the timer
            hours = timer.hour

            // Log.d("test", "$changeInNetForce")
            if (distance > 0)
            {
                newtonLaws.calculateNetForce(changeInNetForce) // Calculate action force
                newtonLaws.calculateAirDragForce() // Calculate reaction force

                // calculate velocity after 100 km
                if (distance % 100 == 0)
                    newtonLaws.calculateVelocity(hours)
                // if velocity is changed then find the change in velocity
                changeInVelocity = newtonLaws.findChangeInVelocity()
                // Log.d("test", "$changeInVelocity")

                // Check if rocket is not already launched or it is not in cool down period
                // Only launch rocket if player has collected any of it
                if (rocket.isLaunched && rocket.isCoolDown)
                {
                    rocket.move(canvas) // Move rocket towards target
                    // Check if rocket has hit the target
                    for (enemy: Enemy in enemies)
                        rocket.checkCollidedWithTarget(enemy)
                }
                else
                {
                    // Reset rocket position
                    rocket.reSpawn(skyDiver.objectContainer.centerX(), skyDiver.objectContainer.centerY())
                    rocket.startCoolDownTime(rocketCoolDownTimer) // Start rocket cool down timer
                }

                // Sky Diver character controls
                skyDiver.drawGameObject(canvas)
                skyDiver.movementValue = skyDiverPosition
                skyDiver.move(canvas)

                // Check if sky diver has interacted with any pickable object
                for (pickableObject: PickableObject in pickableObjects)
                    skyDiver.collectPickableObject(pickableObject)

                // Log.d("test", "${pickableObjects[0].isVisible}")
            }

            if (distance > 2200)
            {
                // Respawn pickable object after when sky diver travels 5000 Km
                if (distance % 1000 == 0 && !pickableObjects[0].isVisible)
                {
                    // Spawn pickable object in random position
                    pickableObjects[0].spawnOnRandomXPosition(100, (canvas.width - skyDiver.width))
                    pickableObjects[0].isVisible = true
                }
                // Log.d("test", "${pickableObjects[0].isVisible} ${pickableObjects[0].x} ${pickableObjects[0].y}")
                pickableObjects[0].move(canvas)

                // If velocity is changed then change enemies velocity
                for (enemy: Enemy in enemies)
                    enemy.applyChangeInVelocity(changeInVelocity) // change velocity

                if (timer.seconds % 349 == 0)
                {
                    enemies[0].isMoving = true
                    enemies[0].spawnOnRandomXPosition(0, 300)
                }
                enemies[0].move(canvas)

                if (timer.seconds % 549 == 0) {
                    enemies[1].isMoving = true
                    enemies[1].spawnOnRandomXPosition(500, 700)
                }
                enemies[1].move(canvas)

                // Log.d("test", "${enemies[0].dx} ${enemies[0].dy}")
                // Log.d("test", "$changeInVelocity ${enemies[0].objectVelocityChange}")

                for (animatedObject: AnimatedObject in clouds) {
                    animatedObject.applyChangeInVelocity(changeInVelocity)
                    animatedObject.move(canvas)
                }
            }
            else
            {
                if (distance > 0)
                {
                    ocean.applyChangeInVelocity(changeInVelocity)
                    ocean.move(canvas)

                    // submarine.drawGameObject(canvas)
                    submarine.applyChangeInVelocity(changeInVelocity)
                    submarine.move(canvas)
                }
                else
                {
                    ocean.drawGameObject(canvas)
                    skyDiver.reSpawn(0, 1200)
                }
                // Log.d("test", "${submarine.x} ${submarine.y}")
            }

            // distance -= newtonLaws.velocity
            distance -= (newtonLaws.velocity / 10)
            if (distance <= 0) {
                submarine.goUnderWater(canvas)
                distanceCovered = totalDistance
                distance = 0
            }
            else
            {
                distanceCovered += (newtonLaws.velocity / 10)
            }

            if (skyDiver.isCollidedWithEnemy(enemies) || submarine.isSubmarineOutOfBound(canvas))
            {
                canvas.drawText("Press the pause button to choose your options", 10f, 700f, objectPaint)
                isEnded = true
            }

            // Draw all values needed to apply newton's law of motions
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), 350f, containerPaint)

            canvas.drawRect(0f, 0f, (canvas.width.toFloat()/2), 120f, paintObject.redColour())
            canvas.drawText("Net Force: ${newtonLaws.netForce} N", 80f, 100f, objectPaint)
            downArrowIcon.drawIcon(2, 50, canvas)

            canvas.drawRect(0f, 120f, (canvas.width.toFloat()/2), 240f, paintObject.lightPrimaryColour())
            canvas.drawText("Air Drag: ${newtonLaws.airDragForce} N", 80f, 200f, objectPaint)
            upArrowIcon.drawIcon(2, 150, canvas)

            canvas.drawRect(0f, 240f, (canvas.width.toFloat()/2), 350f, paintObject.darkGrayColour())
            canvas.drawText("Acceleration: ${newtonLaws.acceleration} km/h^2", 10f, 300f, objectPaint)

            canvas.drawText("Distance: $distance m", 560f, 100f, objectPaint)

            if (newtonLaws.velocity < 130)
                paintObject.paint.color = Color.GREEN
            else if (newtonLaws.velocity in 131..180)
                paintObject.paint.color = Color.YELLOW
            else if (newtonLaws.velocity >= 180)
                paintObject.paint.color = Color.RED

            canvas.drawRect((canvas.width.toFloat()/2), 120f, canvas.width.toFloat(), 240f, paintObject.paint)
            canvas.drawText("Velocity: ${newtonLaws.velocity} km/h", 560f, 200f, objectPaint)

            canvas.drawText("Mass: ${newtonLaws.mass} kg", 600f, 300f, objectPaint)
            canvas.drawText("Cool down: ${rocketCoolDownTimer.seconds}", 10f, 400f, objectPaint)
            canvas.drawText("Missiles: ${skyDiver.pickableObjects.size}", 700f, 400f, objectPaint)

            myHolder.unlockCanvasAndPost(canvas)
        }
    }

    // Stop the current running thread
    fun stop()
    {
        isPaused = true
        while(true)
        {
            try {
                myThread.join()
                break
            }
            catch (e: InterruptedException)
            {
                e.printStackTrace()
            }
            break
        }
    }

    // Resume the current paused or stopped thread
    fun start()
    {
        isPaused = false
        myThread = Thread(this)
        myThread.start()
    }
}