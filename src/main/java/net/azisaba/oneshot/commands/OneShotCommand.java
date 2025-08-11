package net.azisaba.oneshot.commands;

import net.azisaba.oneshot.Chat;
import net.azisaba.oneshot.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import java.util.List;

public class OneShotCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /oneshot <player>");
            return true;
        }
        switch (args[0]) {
            case "ready" -> {
                if(args.length >= 3) {
                    Player a = Bukkit.getPlayer(args[1]);
                    Player b = Bukkit.getPlayer(args[2]);
                    if(a == null || b == null) {
                        sender.sendMessage("Player not found");
                        return true;
                    }
                    if(Game.getInstance().isStarted) {
                        sender.sendMessage("Game already started");
                        return true;
                    }
                    Game.getInstance().ready(a, b);
                }
            }
            case "start" -> {
                if(Game.getInstance().isStarted) {
                    sender.sendMessage("Game already started");
                    return true;
                }
                if(Game.getInstance().startCountdown()) {
                    sender.sendMessage(Chat.f("&eゲームを開始します..."));
                } else {
                    sender.sendMessage(Chat.f("&cゲームを開始することができません"));
                }
            }
            case "end" -> {
                if(!Game.getInstance().isStarted) {
                    sender.sendMessage("Game not started");
                    return true;
                }
                Game.getInstance().end(null);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length == 1) {
            return List.of("ready", "start", "end");
        }
        return null;
    }
}
