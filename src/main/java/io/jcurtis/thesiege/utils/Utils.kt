package io.jcurtis.thesiege.utils

import org.bukkit.Location

class Utils {
    companion object {
        fun isLocationInRadius(location: Location, target: Location, width: Int): Boolean {
            return location.x in target.x - width..target.x + width && location.z in target.z - width..target.z + width
        }
    }
}