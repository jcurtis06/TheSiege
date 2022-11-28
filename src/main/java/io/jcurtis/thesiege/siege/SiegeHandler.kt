package io.jcurtis.thesiege.siege

import io.jcurtis.thesiege.TheSiege
import io.jcurtis.thesiege.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Listener

class SiegeHandler(var defaultLoc: Location) {
    val sieges = mutableListOf<Siege>()

    init {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(TheSiege.instance!!, Runnable {
            if (Bukkit.getWorld("world")!!.time in 13001..13999) {
                if (sieges.isEmpty()) {
                    createSiege(3, 30)
                }
            } else if (sieges.isNotEmpty() && defaultLoc.world!!.time in 0..1000) {
                sieges.forEach {
                    it.endSiege()
                }
                sieges.clear()
            }
        }, 0, 20)
    }

    fun createSiege(maxWaves: Int, radius: Int, target: Location = defaultLoc): Siege? {
        val siege = Siege(target, radius, maxWaves, 1)
        Bukkit.getOnlinePlayers().forEach {
            if (Utils.isLocationInRadius(it.location, target, radius)) {
                it.sendMessage("A siege has started at ${target.x}, ${target.z}, and you are in it! MWAHAAHHAHAHAH!")
                siege.addPlayer(it)
            }
        }
        if (siege.players.isEmpty()) {
            println("No players in siege, cancelling")
            return null
        }
        sieges.add(siege)
        siege.spawnEntitiesRand(EntityType.ZOMBIE)
        return siege
    }

    fun endSiege(siege: Siege) {
        siege.endSiege()
        sieges.remove(siege)
    }

    fun removeSiege(siege: Siege) {
        sieges.remove(siege)
    }

    fun getSiege(location: Location): Siege? {
        try {
            return sieges.first { it.isLocationInSiege(location) }
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    fun getSiege(entity: Entity): Siege {
        return sieges.first { it.isEntityInSiege(entity) }
    }

    fun getSiege(player: Player): Siege {
        return sieges.first { it.isPlayerInSiege(player) }
    }

    fun isSiege(location: Location): Boolean {
        return sieges.any { it.isLocationInSiege(location) }
    }
}