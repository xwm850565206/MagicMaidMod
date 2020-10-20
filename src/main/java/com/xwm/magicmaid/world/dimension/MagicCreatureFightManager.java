package com.xwm.magicmaid.world.dimension;

import com.xwm.magicmaid.entity.mob.basic.AbstructEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.UUID;

public interface MagicCreatureFightManager
{
    NBTTagCompound getCompound();

    void onBossUpdate(AbstructEntityMagicCreature boss);

    void tick();

    void init(AbstructEntityMagicCreature boss);

    void setBossAlive(boolean bossAlive);

    void setBossKilled(boolean bossKilled);

    void setBossuuid(UUID uuid);

    void setWorld(WorldServer world);

    void setBoss(AbstructEntityMagicCreature boss);

    void setBossPos(BlockPos pos);

    boolean getBossAlive();

    boolean getBossKilled();

    UUID getBossuuid();

    WorldServer getWorld();

    AbstructEntityMagicCreature getBoss();

    BlockPos getBossPos();

    void addPlayer(EntityPlayerMP player);

    void removePlayer(EntityPlayerMP player);

    /**
     * 是否是原版不带mod物品的
     * @return
     */
    boolean isOriginMode();
}
