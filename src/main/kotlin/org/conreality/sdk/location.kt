/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

/**
 * Data class for representing a pair of latitude and longitude coordinates.
 *
 * @property latitude  Latitude, in degrees. Range [-90, 90].
 * @property longitude Longitude, in degrees. Range [-180, 180).
 * @property altitude  Altitude, in meters above the WGS 84 reference ellipsoid.
 */
data class Location(val latitude: Double, val longitude: Double, val altitude: Double? = null)
