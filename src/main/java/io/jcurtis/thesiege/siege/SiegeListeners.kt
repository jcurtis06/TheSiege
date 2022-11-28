package io.jcurtis.thesiege.siege

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

class SiegeListeners(private val handler: SiegeHandler): Listener {
    @EventHandler
    fun playerMove(e: PlayerMoveEvent) {
        val fromSiege = handler.getSiege(e.from)

        if (fromSiege != null) {
            if (e.to == null) return
            if (handler.getSiege(e.to!!) != handler.getSiege(e.from)) {
                e.player.sendMessage("You cannot leave the siege!")
                e.isCancelled = true
            }
        }

        if (e.to == null) return
        val toSiege = handler.getSiege(e.to!!)
        if (toSiege != null && !toSiege.players.contains(e.player.uniqueId)) {
            e.player.sendMessage("You can't enter this area because there's a siege happening!")
            e.isCancelled = true
        }
    }

    @EventHandler
    fun entDeath(e: EntityDeathEvent) {
        val siege = handler.getSiege(e.entity)
        if (siege != null) {
            siege.removeEntity(e.entity)
        }
    }

    @EventHandler
    fun playerDeath(e: PlayerDeathEvent) {
        val siege = handler.getSiege(e.entity)
        if (siege != null) {
            siege.removePlayer(e.entity)
        }
    }

    @EventHandler
    fun playerLeave(e: PlayerQuitEvent) {
        val siege = handler.getSiege(e.player)
        if (siege != null) {
            siege.removePlayer(e.player)
        }
    }
}