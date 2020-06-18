// 
// Decompiled by Procyon v0.5.36
// 

package eu.mip.alandioda.region.spigot;

import org.bukkit.Location;

public class Region
{
    String regionName;
    boolean isEnabled;
    Location boundMax;
    Location boundMin;
    main.PlayerSetting playerCD;
    main.PlayerSetting projectileCD;
    main.PlayerSetting playerCBD;
    main.PlayerSetting fallDD;
    main.PlayerSetting playersCD;
    main.PlayerSetting entityCD;
    main.PlayerSetting hungerChange;
    main.PlayerSetting interacts;
    main.PlayerSetting cBuild;
    main.PlayerSetting cBreak;
    main.PlayerSetting pickUpItems;
    main.PlayerSetting dropItems;
    main.Setting explosionDamage;
    main.Setting grow;
    
    public Region() {
        this.playerCD = main.PlayerSetting.OFF;
        this.projectileCD = main.PlayerSetting.OFF;
        this.playerCBD = main.PlayerSetting.OFF;
        this.fallDD = main.PlayerSetting.OFF;
        this.playersCD = main.PlayerSetting.OFF;
        this.entityCD = main.PlayerSetting.OFF;
        this.hungerChange = main.PlayerSetting.OFF;
        this.interacts = main.PlayerSetting.OFF;
        this.cBuild = main.PlayerSetting.OFF;
        this.cBreak = main.PlayerSetting.OFF;
        this.pickUpItems = main.PlayerSetting.OFF;
        this.dropItems = main.PlayerSetting.OFF;
        this.explosionDamage = main.Setting.OFF;
        this.grow = main.Setting.OFF;
    }
}
