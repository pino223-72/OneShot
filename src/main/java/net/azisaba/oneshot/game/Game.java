package net.azisaba.oneshot.game;

import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSMinion;
import com.shampaggon.crackshot.CSUtility;
import net.azisaba.oneshot.Chat;
import net.azisaba.oneshot.OneShot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Game {

    private static Game instance;

    private Player a;
    private Player b;
    public boolean isStarted = false;

    public void ready(Player a, Player b) {
        if(isStarted){
            return;
        }

        this.a = a;
        this.b = b;

        a.teleportAsync(OneShot.getInstance().getMainConfig().getRoom());
        b.teleportAsync(OneShot.getInstance().getMainConfig().getRoom());

        a.getWorld().getPlayers().forEach(p -> p.sendTitle(Chat.f("&e早撃ち対決"), Chat.f(a.getName() + " &evs &f" + b.getName()), 0, 20, 0));
        a.getWorld().getPlayers().forEach(p -> p.sendMessage(Chat.f("&e[OneShot] &f" + a.getName() + " &evs &f" + b.getName() + " まもなく開始")));
    }

    public boolean startCountdown() {
        if(a == null || b == null || !a.isOnline() || !b.isOnline() || isStarted) {
            return false;
        }

        isStarted = true;

        final int[] count = {5};
        Bukkit.getScheduler().runTaskTimer(OneShot.getInstance(), task -> {
            if(a == null || b == null || !a.isOnline() || !b.isOnline() || !isStarted) {
                task.cancel();
                return;
            }
            if (count[0] <= 0) {
                task.cancel();
                start();
                return;
            }

            a.getWorld().getPlayers().forEach(p -> p.sendTitle(Chat.f("&c" + count[0]), " ", 1, 20, 10));
            a.getWorld().getPlayers().forEach(p -> p.sendMessage(Chat.f("&f[OneShot] &e開始まであと " + count[0] + " 秒...")));
            count[0]--;
        }, 0, 20L);

        return true;
    }

    public void start() {
        if(a == null || b == null || !a.isOnline() || !b.isOnline() || !isStarted) {
            return;
        }

        a.teleportAsync(OneShot.getInstance().getMainConfig().getSpawnA());
        b.teleportAsync(OneShot.getInstance().getMainConfig().getSpawnB());

        a.getWorld().dropItem(a.getLocation() , new CSUtility().generateWeapon(OneShot.getInstance().getMainConfig().getWeapon())).setPickupDelay(0);
        b.getWorld().dropItem(b.getLocation() , new CSUtility().generateWeapon(OneShot.getInstance().getMainConfig().getWeapon())).setPickupDelay(0);

        a.getWorld().getPlayers().forEach(p -> p.sendTitle(Chat.f("&a&lSTART"), " ", 0, 20, 10));
        a.getWorld().getPlayers().forEach(p -> p.sendMessage(Chat.f("&e[OneShot] &f" + a.getName() + " &evs &f" + b.getName() + " 試合開始！")));
    }

    public void end(Player winner) {
        if (a == null || b == null || !a.isOnline() || !b.isOnline() || !isStarted) {
            return;
        }

        isStarted = false;
        ItemStack item = new CSUtility().generateWeapon(OneShot.getInstance().getMainConfig().getWeapon());
        a.getInventory().removeItem(item);
        b.getInventory().removeItem(item);

        if(winner == null) {
            //draw
            a.getWorld().getPlayers().forEach(p -> p.sendTitle(Chat.f("&e引き分け"), " ", 0, 20, 10));
            a.getWorld().getPlayers().forEach(p -> p.sendMessage(Chat.f("&e[OneShot] " + a.getName() + " &evs &f" + b.getName() + " --→ &e引き分け")));

            Bukkit.getScheduler().runTaskLater(OneShot.getInstance(), () -> {
                if(a == null || b == null || !a.isOnline() || !b.isOnline()){
                    return;
                }

                a.teleportAsync(OneShot.getInstance().getMainConfig().getRoom());
                b.teleportAsync(OneShot.getInstance().getMainConfig().getRoom());

                this.ready(a, b);
            }, 80L);
        }else {
            winner.sendTitle(Chat.f("&a&lVictory!"), " ", 0, 60, 0);
            Player loser = a == winner ? b : a;
            loser.sendTitle(Chat.f("&c&lDEFEAT..."), " ", 0, 60, 0);
            loser.teleportAsync(OneShot.getInstance().getMainConfig().getRoom());
            winner.getWorld().getPlayers().forEach(p -> p.sendMessage(Chat.f("&e[OneShot] &f" + a.getName() + " &evs &f" + b.getName() + " --→ &a" + winner.getName() + " の勝利！")));

            Bukkit.getScheduler().runTaskLater(OneShot.getInstance(), () -> {
                if(winner == null || !winner.isOnline()){
                    return;
                }
                winner.teleportAsync(OneShot.getInstance().getMainConfig().getWinner());
            },60L);
        }
    }

    public Player getA() {
        return a;
    }

    public Player getB() {
        return b;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

}
