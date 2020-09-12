package com.xwm.magicmaid.world.dimension;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMarthaBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRettBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelinaBoss;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class MagicMaidFightManager
{
    public boolean bossAlive;
    public boolean bossKilled;
    public UUID bossuuid;
    public WorldServer world;
    public EntityMagicMaid boss;
    public BlockPos bossPos;
    public List<EntityPlayerMP> playerList;

    public int bossType;

    public MagicMaidFightManager(WorldServer worldIn, NBTTagCompound compound)
    {
        this.world = worldIn;
        this.playerList = new Vector<>();
        this.bossType = 0;

        if (compound.hasKey("bossAlive")) {
            bossAlive = compound.getBoolean("bossAlive");
            bossKilled = compound.getBoolean("maid_killed");

            if (compound.hasKey("maid_uuid")) {
                bossuuid = compound.getUniqueId("maid_uuid");
                boss = EntityMagicMaid.getMaidFromUUID(world, bossuuid);
                if (boss != null)
                    bossPos = boss.getPosition();
                bossType = getBossType();
            }
        }
        else {
            bossAlive = false;
            bossKilled = false;
        }

        if (bossPos == null) {
            //todo 默认的boss位置
            bossPos = new BlockPos(100, 55, 0);
        }
    }

    NBTTagCompound getCompound(){
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (bossuuid != null) {
            nbttagcompound.setUniqueId("maid_uuid", bossuuid);
        }

        nbttagcompound.setBoolean("bossAlive", bossAlive);
        nbttagcompound.setBoolean("maid_killed", bossKilled);

        /*NBTTagCompound players = new NBTTagCompound();
        for (EntityPlayer player : playerList) {
            players.setUniqueId(player.getUniqueID().toString(), player.getUniqueID());
        }
        nbttagcompound.setTag("playerList", players);*/

        return nbttagcompound;
    }


    public int getBossType() {
        if (bossType != 0)
            return bossType;
        else {
            if (boss == null)
                bossType = 0;
            else if (boss instanceof EntityMagicMaidMarthaBoss)
                bossType = 1;
            else if (boss instanceof EntityMagicMaidRettBoss)
                bossType = 2;
            else
                bossType = 3;
            return bossType;
        }
    }

    protected EntityMagicMaid getBoss() {

        switch (bossType) {
            case 0: return null;
            case 1: return new EntityMagicMaidMarthaBoss(world);
            case 2: return new EntityMagicMaidRettBoss(world);
            case 3: return new EntityMagicMaidSelinaBoss(world);
            default: return null;
        }
    }

    public void onBossUpdate(EntityMagicMaid boss)
    {
        this.boss = boss;
        bossPos = boss.getPosition();
    }
    public void tick()
    {
        if (bossAlive) {
            bossKilled = false;

            if (boss == null || boss.isDead || boss.getTrueHealth() <= 0) {
                boss = getBoss();
                if (boss != null) {
                    boss.setUniqueId(bossuuid);
                    boss.setPosition(bossPos.getX(), bossPos.getY(), bossPos.getZ());
                    world.spawnEntity(boss);
                    world.setEntityState(boss, (byte) 38);
                    for (EntityPlayerMP player : playerList) {
                        PunishOperationHandler.punishPlayer(player, 1, "检测到boss被意外清除，尝试清空玩家背包并重生boss");
                    }
                }
            }
        }
        else {
            bossType = 0;
            bossKilled = true;
            bossuuid = null;
        }
    }

    public void init(EntityMagicMaid boss)
    {
        this.bossAlive = true;
        this.bossKilled = false;
        this.bossuuid = boss.getUniqueID();
        this.boss = boss;
        this.bossPos = boss.getPosition();
        this.bossType = this.getBossType();
    }

}
