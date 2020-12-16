package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.UUID;

public interface IMagicBossManager
{
    NBTTagCompound getCompound();

    void onBossUpdate(AbstractEntityMagicCreature boss);

    void tick();

    void init(AbstractEntityMagicCreature boss);

    void setBossAlive(boolean bossAlive);

    void setBossKilled(boolean bossKilled);

    void setBossuuid(UUID uuid);

    void setWorld(WorldServer world);

    void setBoss(AbstractEntityMagicCreature boss);

    void setBossPos(BlockPos pos);

    boolean getBossAlive();

    boolean getBossKilled();

    UUID getBossuuid();

    WorldServer getWorld();

    AbstractEntityMagicCreature getBoss();

    BlockPos getBossPos();

    void addPlayer(EntityPlayerMP player);

    void removePlayer(EntityPlayerMP player);

    /**
     * 是否是原版不带mod物品的
     * @return
     */
    boolean isOriginMode();
}
