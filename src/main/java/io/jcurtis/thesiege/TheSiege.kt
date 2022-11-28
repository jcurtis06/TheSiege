package io.jcurtis.thesiege

import io.jcurtis.thesiege.siege.SiegeHandler
import io.jcurtis.thesiege.siege.SiegeListeners
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin


/*
The siege is a plugin that generates a siege in a radius around a location.
The siege can be started naturally or by a command.
During a siege, players cannot sleep, and lots of monsters spawn around the chosen location.
The siege ends when day comes
If players attempt to leave the siege, they are teleported back to the siege.
Only players that were in the siege when it started may participate in the siege.
If other players attempt to enter the siege, they are switched to spectator mode.
Players cannot leave the siege until the siege ends.
If players die during the siege, they are removed from the siege and cannot rejoin.
If all players die before morning, the siege ends.

Users can set siege locations and radii in the config file.
Sieges occur each night one of these locations (chosen randomly).
 */

class TheSiege : JavaPlugin() {
    companion object {
        var instance: TheSiege? = null
    }

    override fun onEnable() {
        instance = this

        val handler = SiegeHandler(Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0))

        // register events
        server.pluginManager.registerEvents(SiegeListeners(handler), this)

        val msg = ChatColor.translateAlternateColorCodes(
            '&',
            "&a[⊐&4•̀&a⌂&4•́&a]⊐ &1Prepare yourself, mortals! The Siege is enabled!"
        )
        server.consoleSender.sendMessage(msg)
    }

    override fun onDisable() {
        val msg = ChatColor.translateAlternateColorCodes('&', "&a[⊐&4•̀&a⌂&4•́&a]⊐ &1The Siege has been disabled. You may now rest in peace.")
        server.consoleSender.sendMessage(msg)
    }
}