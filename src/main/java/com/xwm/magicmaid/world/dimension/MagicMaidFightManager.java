package com.xwm.magicmaid.world.dimension;

import com.xwm.magicmaid.entity.mob.basic.AbstructEntityMagicCreature;
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

public class MagicMaidFightManager implements MagicCreatureFightManager
{
    private boolean bossAlive;
    private boolean bossKilled;
    private UUID bossuuid;
    private WorldServer world;
    private EntityMagicMaid boss;
    private BlockPos bossPos;
    private int bossType;

    public List<EntityPlayerMP> playerList;



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

    public NBTTagCompound getCompound(){
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (bossuuid != null) {
            nbttagcompound.setUniqueId("maid_uuid", bossuuid);
        }

        nbttagcompound.setBoolean("bossAlive", bossAlive);
        nbttagcompound.setBoolean("maid_killed", bossKilled);

        return nbttagcompound;
    }

    @Override
    public void onBossUpdate(AbstructEntityMagicCreature boss) {
        bossPos = boss.getPosition();
        if (bossPos.getY() < world.provider.getAverageGroundLevel())
            boss.setPosition(bossPos.getX(), 55, bossPos.getZ()); //防止boss掉下虚空
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

    public AbstructEntityMagicCreature getBoss() {

        switch (bossType) {
            case 0: return null;
            case 1: return new EntityMagicMaidMarthaBoss(world);
            case 2: return new EntityMagicMaidRettBoss(world);
            case 3: return new EntityMagicMaidSelinaBoss(world);
            default: return null;
        }
    }

    @Override
    public BlockPos getBossPos() {
        return bossPos;
    }

    @Override
    public void addPlayer(EntityPlayerMP player) {
        this.playerList.add(player);
    }

    @Override
    public void removePlayer(EntityPlayerMP player) {
        this.playerList.remove(player);
    }

    public void onBossUpdate(EntityMagicMaid boss)
    {
        this.boss = boss;
        this.bossPos = boss.getPosition();
    }

    public void tick()
    {
        if (bossAlive) {
            bossKilled = false;

            if (boss == null || boss.isDead || boss.getTrueHealth() <= 0) {
                boss = (EntityMagicMaid) getBoss();
                if (boss != null) {
                    boss.setUniqueId(bossuuid);
                    boss.setPosition(bossPos.getX(), bossPos.getY(), bossPos.getZ());
                    world.spawnEntity(boss);
                    world.setEntityState(boss, (byte) 38);

                    init(boss);

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

    @Override
    public void init(AbstructEntityMagicCreature boss) {
        this.bossAlive = true;
        this.bossKilled = false;
        this.bossuuid = boss.getUniqueID();
        this.boss = (EntityMagicMaid) boss;
        this.bossPos = boss.getPosition();
        this.bossType = this.getBossType();
    }

    @Override
    public void setBossAlive(boolean bossAlive) {
        this.bossAlive = bossAlive;
    }

    @Override
    public void setBossKilled(boolean bossKilled) {
        this.bossKilled = bossKilled;
    }

    @Override
    public void setBossuuid(UUID uuid) {
        this.bossuuid = uuid;
    }

    @Override
    public void setWorld(WorldServer world) {
        this.world = world;
    }

    @Override
    public void setBoss(AbstructEntityMagicCreature boss) {
        this.boss = (EntityMagicMaid) boss;
    }

    @Override
    public void setBossPos(BlockPos pos) {
        this.bossPos = pos;
    }

    @Override
    public boolean getBossAlive() {
        return this.bossAlive;
    }

    @Override
    public boolean getBossKilled() {
        return this.bossKilled;
    }

    @Override
    public UUID getBossuuid() {
        return this.bossuuid;
    }

    @Override
    public WorldServer getWorld() {
        return this.world;
    }
}
