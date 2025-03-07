package net.azisaba.oneshot.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(Game.getInstance().isStarted) {
            if(e.getEntity() == Game.getInstance().getA()) {
                Game.getInstance().end(Game.getInstance().getB());
            }else if(e.getEntity() == Game.getInstance().getB()) {
                Game.getInstance().end(Game.getInstance().getA());
            }
        }
    }
}
