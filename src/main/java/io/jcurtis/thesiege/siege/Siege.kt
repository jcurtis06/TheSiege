package io.jcurtis.thesiege.siege

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import java.util.UUID

/*
This class is responsible for holding data for a specific siege.
 */
class Siege(val target: Location, val radius: Int, val maxWaves: Int, var wave: Int) {
    val players = mutableListOf<UUID>()
    val entities = mutableListOf<Entity>()
    val bossBar = Bukkit.createBossBar("Zombie Siege | Wave $wave", BarColor.RED, BarStyle.SOLID)

    fun addPlayer(player: Player) {
        players.add(player.uniqueId)
        bossBar.addPlayer(player)
    }

    fun removePlayer(player: Player) {
        players.remove(player.uniqueId)
        bossBar.removePlayer(player)
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun removeEntity(entity: Entity) {
        entities.remove(entity)
        if (entities.isEmpty()) {
            wave++
            if (wave > maxWaves) {
                println("Siege complete!")
            } else {
                bossBar.setTitle("Zombie Siege | Wave $wave")
                spawnEntitiesRand(EntityType.ZOMBIE)
            }
        }
    }

    fun incrementWave() {
        wave++
        bossBar.setTitle("The Siege | Wave $wave")
    }

    fun isComplete(): Boolean {
        return wave >= maxWaves
    }

    fun isPlayerInSiege(player: Player): Boolean {
        return players.contains(player.uniqueId)
    }

    fun isEntityInSiege(entity: Entity): Boolean {
        return entities.contains(entity)
    }

    fun isLocationInSiege(location: Location): Boolean {
        return location.x in target.x - radius..target.x + radius && location.z in target.z - radius..target.z + radius
    }

    fun endSiege() {
        players.forEach {
            val player = Bukkit.getPlayer(it)
            if (player != null) {
                player.sendMessage("Daylight has come... The siege is over.")
                bossBar.removePlayer(player)
            }
        }
        entities.forEach {
            it.remove()
        }
    }

    fun spawnEntitiesRand(ent: EntityType) {
        for (i in 0..wave*10) {
            val potentialSpawnLoc = target.clone()
            potentialSpawnLoc.add((Math.random() * radius * 2) - radius, 0.0, (Math.random() * radius * 2) - radius)
            potentialSpawnLoc.y = target.world!!.getHighestBlockYAt(potentialSpawnLoc)!!.toDouble()
            val entity = target.world!!.spawnEntity(potentialSpawnLoc.add(0.0, 1.0, 0.0), ent) as Monster
            entity.target = Bukkit.getPlayer(players[0])
            entities.add(entity)
        }
    }
}