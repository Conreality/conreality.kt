/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

/**
 * Data class for representing an object position with 3D coordinates.
 *
 * @property x The X coordinate, in meters from the origin.
 * @property y The Y coordinate, in meters from the origin.
 * @property z The Z coordinate, in meters from the origin.
 */
data class Position(val x: Double, val y: Double, val z: Double)
