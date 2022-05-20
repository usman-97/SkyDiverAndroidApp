package com.usk.skydiver

class NewtonLaws(var acceleration: Int, var airDragForce: Int, var mass: Int)
{
    private val initialNetForce: Int = (mass * 9.80).toInt()
    private val initialVelocity = 100
    private val defaultNetForce: Float = 918.5f
    private val netForceLimit = 1000
    private val finalVelocity = 200

    var netForce: Int = initialNetForce
    var velocity: Int = initialVelocity
    var tempVelocity: Int = 0

    // Calculate acceleration using newton's second law of motion
    private fun calculateAcceleration()
    {
        if (netForce == initialNetForce)
            acceleration = 0
        else
            acceleration = netForce / mass
    }

    // Calculate total net force
    fun calculateNetForce(newNetForceValue: Float)
    {
        increaseNetForce(newNetForceValue)
        decreaseNetForce(newNetForceValue);
        if (netForce == initialNetForce)
            acceleration = 0
        else
            calculateAcceleration()
    }

    private fun increaseNetForce(newNetForceValue: Float)
    {
        if (netForce in initialNetForce..netForceLimit) {
            // if (newNetForceValue > (defaultNetForce + 150)) {
            if (newNetForceValue > defaultNetForce) {
                netForce++
            }
        }
    }

    private fun decreaseNetForce(newNetForceValue: Float)
    {
        if (netForce > initialNetForce)
            if (newNetForceValue < defaultNetForce)
                netForce--
    }

    fun calculateAirDragForce()
    {
        if (airDragForce != netForce)
        {
            if (airDragForce > netForce)
                airDragForce--
            if (airDragForce < netForce)
                airDragForce++
        }
    }

    // Calculate total velocity
    fun calculateVelocity(hours: Int)
    {
        tempVelocity = initialVelocity + (acceleration * hours)
        if (velocity < (finalVelocity + 1))
        {
            if (tempVelocity > velocity)
            {
                velocity++
            }
            else if (tempVelocity > velocity)
            {
                velocity--
            }
        }
    }

    // Calculate change in velocity
    fun findChangeInVelocity(): Int
    {
        return velocity - initialVelocity
    }
}