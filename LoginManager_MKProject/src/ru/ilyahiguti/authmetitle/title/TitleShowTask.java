package ru.ilyahiguti.authmetitle.title;

import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mkproject.maskat.LoginManager.Plugin;

public class TitleShowTask {
    //private final AuthMeTitle plugin;
    private final Player player;
    private final List<Title> titleFrames;
    private final Predicate<Player> isNeedToStop;
    private final boolean repeat;
    private final int repeatFrom;
    private final int lastFrame;
    private int currFrame = -1;

    TitleShowTask(Player player, List<Title> titleFrames, boolean repeat, int repeatFrom, Predicate<Player> isNeedToStop) {//, AuthMeTitle plugin
        this.player = player;
        this.titleFrames = titleFrames;
        this.repeat = repeat;
        this.lastFrame = titleFrames.size() - 1;
        this.repeatFrom = repeatFrom;
        this.isNeedToStop = isNeedToStop;
        //this.plugin = plugin;
        nextFrame();
    }

    private void nextFrame() {
        if (!player.isOnline()) return;
        
        try {
        	if (isNeedToStop.test(player)) return;
        }
        catch(Exception e)
        {
        	return;
        }
        	
        //if(Papi.Model.getPlayer(player).isLogged()) return;

        if (currFrame == lastFrame) {
            if (repeat) {
                currFrame = repeatFrom;
            } else {
                return;
            }
        }

        Title frame = titleFrames.get(++currFrame);

        Plugin.getNMS().sendTitle(player, frame.getTitle(), frame.getSubtitle(), frame.getFadeIn(), frame.getStay(), frame.getFadeOut());
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(), this::nextFrame, frame.getDelayNext());
    }
}
