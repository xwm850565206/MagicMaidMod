package com.xwm.magicmaid.world.dimension;

import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMarthaBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRettBoss;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidSelinaBoss;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.handlers.PunishOperationHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

import java.util.*;

public class MagicMaidFightManager implements MagicCreatureFightManager
{
    /**
     * 白名单，使用这些mod，boss不会被强化
     */
    public static HashSet<String> whiteDomain = new HashSet<String>(){{
        add(Reference.MODID);
        add("stoneage");
        add("minecraft");
    }};

    private boolean bossAlive;
    private boolean bossKilled;
    private UUID bossuuid;
    private WorldServer world;
    private EntityMagicMaid boss;
    private BlockPos bossPos;
    private int bossType;
    private boolean isOrigin;
    private Random random;

    public List<EntityPlayerMP> playerList;


    public MagicMaidFightManager(WorldServer worldIn, NBTTagCompound compound)
    {
        this.world = worldIn;
        this.playerList = new Vector<>();
        this.bossType = 0;
        this.isOrigin = true;
        this.random = new Random();

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
    public void onBossUpdate(AbstractEntityMagicCreature boss) {
        bossPos = boss.getPosition();
        this.boss = (EntityMagicMaid) boss;
        if (!isOrigin) {
            ((IEntityBossCreature) this.boss).setBossDamageFactor(10);
            //添加boss愤怒标记
            for (EntityPlayer player : playerList) {
                PotionEffect effect = player.getActivePotionEffect(PotionInit.BOSS_ANGRY_EFFECT);
                if (effect == null || effect.getDuration() <= 1)
                    player.addPotionEffect(new PotionEffect(PotionInit.BOSS_ANGRY_EFFECT, 400, 0));
            }
        }

        if (bossPos.getY() < world.provider.getAverageGroundLevel())
        {
            boolean flag = false;
            for (int i = -5; i <= 5 && !flag; i++)
                for (int j = -5; j <= 5 && !flag; j++)
                    for (int k = 1; k <= 5 && !flag; k++) {

                        BlockPos pos = bossPos.add(i, 50, j);
                        IBlockState state = world.getBlockState(pos);
                        if (state.getBlock().canCreatureSpawn(state, world, pos, EntityLiving.SpawnPlacementType.ON_GROUND))
                        {
                            boss.setPosition(pos.getX(), pos.getY(), pos.getZ()); //防止boss掉下虚空
                            flag = true;
                        }
                    }
            if (!flag)
                boss.setPosition(bossPos.getX() + random.nextInt(6) - 3, 55, bossPos.getZ() + random.nextInt(6) - 3); //防止boss掉下虚空
        }
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

    public AbstractEntityMagicCreature getBoss() {

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
        if (!isOrigin) {
            player.sendMessage(new TextComponentString("检测有玩家携带其他模组，boss增强"));
            player.addPotionEffect(new PotionEffect(PotionInit.BOSS_ANGRY_EFFECT, 400, 0));
        }

        List<NonNullList<ItemStack>> allInventories = Arrays.<NonNullList<ItemStack>>asList(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory);
        for (NonNullList<ItemStack> list : allInventories)
        {
            if (!isOrigin)
                break;
            for (ItemStack stack : list) {
                try {
                    String domain = stack.getItem().getRegistryName().getResourceDomain();
                    if (whiteDomain.contains(domain))
                        continue;
                } catch (NullPointerException e) {
                    continue;
                }
                isOrigin = false;
                for (EntityPlayer player1 : playerList) {
                    player1.sendMessage(new TextComponentString("检测有玩家携带其他模组，boss增强"));
                    player1.addPotionEffect(new PotionEffect(PotionInit.BOSS_ANGRY_EFFECT, 400, 0));
                }
                break;
            }
        }
    }

    @Override
    public void removePlayer(EntityPlayerMP player) {
        this.playerList.remove(player);

        player.removePotionEffect(PotionInit.BOSS_ANGRY_EFFECT);
        if (this.playerList.isEmpty())
            isOrigin = true;
    }

    /**
     * 是否是原版不带mod物品的
     *
     * @return
     */
    @Override
    public boolean isOriginMode() {
        return isOrigin;
    }

    public void tick()
    {
        if (world.isRemote)
            return;

        if (boss == null && getBossAlive()) {
            bossKilled = false;
            if (getBossAlive()) {
                boss = (EntityMagicMaid) getBoss();
                if (boss != null) {
                    boss.setUniqueId(bossuuid);
                    boss.setPosition(bossPos.getX(), bossPos.getY(), bossPos.getZ());
                    world.spawnEntity(boss);

                    init(boss);

                    for (EntityPlayerMP player : playerList) {
                        PunishOperationHandler.punishPlayer(player, 1, "检测到boss被意外清除，尝试清空玩家背包并重生boss");
                    }
                }
            } else {
                bossType = 0;
                bossKilled = true;
                bossuuid = null;
            }
        } else if (boss != null && world.getEntityByID(boss.getEntityId()) == null) {
            if (boss.isDead)
                setBossAlive(false);
            else
                world.spawnEntity(boss);
        }
    }

    @Override
    public void init(AbstractEntityMagicCreature boss) {
        this.bossuuid = boss.getUniqueID();
        this.boss = (EntityMagicMaid) boss;
        this.bossPos = boss.getPosition();
        this.bossType = this.getBossType();
        setBossAlive(true);
        setBossKilled(false);
    }

    @Override
    synchronized public void setBossAlive(boolean bossAlive) {
        this.bossAlive = bossAlive;
        if (!bossAlive) //boss被杀死 重置参数
            isOrigin = true;
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
    public void setBoss(AbstractEntityMagicCreature boss) {
        this.boss = (EntityMagicMaid) boss;
    }

    @Override
    public void setBossPos(BlockPos pos) {
        this.bossPos = pos;
    }

    @Override
    synchronized public boolean getBossAlive() {
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
