package me.eccentric_nz.gamemodeinventories;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

public class GameModeInventoriesXPCalculator {
   private static int hardMaxLevel = 100000;
   private static int[] xpTotalToReachLevel;
   private final WeakReference<Player> player;
   private final String playerName;

   public GameModeInventoriesXPCalculator(Player player) {
      Validate.notNull(player, "Player cannot be null");
      this.player = new WeakReference(player);
      this.playerName = player.getName();
   }

   public static int getHardMaxLevel() {
      return hardMaxLevel;
   }

   public static void setHardMaxLevel(int hardMaxLevel) {
      GameModeInventoriesXPCalculator.hardMaxLevel = hardMaxLevel;
   }

   private static void initLookupTables(int maxLevel) {
      xpTotalToReachLevel = new int[maxLevel];

      for(int i = 0; i < xpTotalToReachLevel.length; ++i) {
         xpTotalToReachLevel[i] = i >= 30 ? (int)(3.5D * (double)i * (double)i - 151.5D * (double)i + 2220.0D) : (i >= 16 ? (int)(1.5D * (double)i * (double)i - 29.5D * (double)i + 360.0D) : 17 * i);
      }

   }

   private static int calculateLevelForExp(int exp) {
      int level = 0;
      int curExp = 7;

      for(int incr = 10; curExp <= exp; incr += level % 2 == 0 ? 3 : 4) {
         curExp += incr;
         ++level;
      }

      return level;
   }

   public Player getPlayer() {
      Player p = (Player)this.player.get();
      if (p == null) {
         throw new IllegalStateException("Player " + this.playerName + " is not online");
      } else {
         return p;
      }
   }

   public void changeExp(int amt) {
      this.changeExp((double)amt);
   }

   public void changeExp(double amt) {
      this.setExp(this.getCurrentFractionalXP(), amt);
   }

   public void setExp(int amt) {
      this.setExp(0.0D, (double)amt);
   }

   public void setExp(double amt) {
      this.setExp(0.0D, amt);
   }

   private void setExp(double base, double amt) {
      int xp = (int)Math.max(base + amt, 0.0D);
      Player p = this.getPlayer();
      int curLvl = p.getLevel();
      int newLvl = this.getLevelForExp(xp);
      if (curLvl != newLvl) {
         p.setLevel(newLvl);
      }

      if ((double)xp > base) {
         p.setTotalExperience(p.getTotalExperience() + xp - (int)base);
      }

      double pct = (base - (double)this.getXpForLevel(newLvl) + amt) / (double)this.getXpNeededToLevelUp(newLvl);
      p.setExp((float)pct);
   }

   public int getCurrentExp() {
      Player p = this.getPlayer();
      int lvl = p.getLevel();
      int cur = this.getXpForLevel(lvl) + Math.round((float)this.getXpNeededToLevelUp(lvl) * p.getExp());
      return cur;
   }

   private double getCurrentFractionalXP() {
      Player p = this.getPlayer();
      int lvl = p.getLevel();
      double cur = (double)this.getXpForLevel(lvl) + (double)((float)this.getXpNeededToLevelUp(lvl) * p.getExp());
      return cur;
   }

   public boolean hasExp(int amt) {
      return this.getCurrentExp() >= amt;
   }

   public boolean hasExp(double amt) {
      return this.getCurrentFractionalXP() >= amt;
   }

   public int getLevelForExp(int exp) {
      if (exp <= 0) {
         return 0;
      } else {
         int pos;
         if (exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]) {
            pos = calculateLevelForExp(exp) * 2;
            Validate.isTrue(pos <= hardMaxLevel, "Level for exp " + exp + " > hard max level " + hardMaxLevel);
            initLookupTables(pos);
         }

         pos = Arrays.binarySearch(xpTotalToReachLevel, exp);
         return pos < 0 ? -pos - 2 : pos;
      }
   }

   public int getXpNeededToLevelUp(int level) {
      Validate.isTrue(level >= 0, "Level may not be negative.");
      return level > 30 ? 62 + (level - 30) * 7 : (level >= 16 ? 17 + (level - 15) * 3 : 17);
   }

   public int getXpForLevel(int level) {
      Validate.isTrue(level >= 0 && level <= hardMaxLevel, "Invalid level " + level + "(must be in range 0.." + hardMaxLevel + ")");
      if (level >= xpTotalToReachLevel.length) {
         initLookupTables(level * 2);
      }

      return xpTotalToReachLevel[level];
   }

   static {
      initLookupTables(25);
   }
}
