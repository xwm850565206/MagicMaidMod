package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import com.xwm.magicmaid.world.dimension.DimensionChurch;
import com.xwm.magicmaid.world.dimension.MagicCreatureFightManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityMagicMaidRettBoss extends EntityMagicMaidRett implements IEntityBossCreature
{
    protected MagicCreatureFightManager fightManager = null;

    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private int factor = 1;


    public EntityMagicMaidRettBoss(World worldIn) {
        super(worldIn);
        this.setMode(EnumMode.toInt(EnumMode.BOSS));
        this.setRank(2);

        this.initFightManager(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if (source.damageType.equals("killed_rett")) {
            try{
                return this.killItSelfByPlayerDamage((EntityPlayer) source.getTrueSource());
            } catch (Exception e){
                return false;
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    @Override
    public void onLivingUpdate()
    {
        this.bossInfo.setName(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()));
        this.bossInfo.setPercent(getHealth() / getMaxHealth());

        if (EnumEquipment.valueOf(this.getWeaponType()) == EnumEquipment.NONE) {
            this.setInventorySlotContents(0, new ItemStack(ItemInit.itemDemonKillerSowrd));
            this.setInventorySlotContents(1, new ItemStack(ItemInit.itemImmortal));
        }

        super.onLivingUpdate();
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        if (getTrueHealth() > 0){
            return;
        }
        else {
            super.onDeath(cause);
        }
    }

    @Override
    public void onDeathUpdate()
    {
        super.onDeathUpdate();
        if (getTrueHealth() > 0)
            this.bossInfo.setName(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()));
        else if (this.deathTime == 20) {
            if (fightManager != null) {
                fightManager.setBossAlive(false); //boss真实死亡
                fightManager.setBossKilled(true);
            }
        }
    }

    @Override
    public void heal(float healAmount)
    {
        super.heal(healAmount);
        this.bossInfo.setName(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()));
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        if (getTrueHealth() > 0) return null;
        return LootTableHandler.DEMONKILLER;
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound compound){
        super.readEntityFromNBT(compound);
        this.bossInfo.setName(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()));
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String name)
    {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    /**
     * 提供斩杀接口
     *
     * @param player
     */
    @Override
    public boolean killItSelfByPlayerDamage(EntityPlayer player) {
        if (player.getEntityWorld().isRemote)
            return true;
        this.setAvoidSetHealth(-1);
        this.setAvoidDamage(-1);
        this.setHealthbarnum(0);
        return super.attackEntityFrom(DamageSource.
                causePlayerDamage(player), getMaxHealth()+1);
    }

    /**
     * 获取战斗管理器
     *
     * @return
     */
    @Override
    public MagicCreatureFightManager getFightManager() {
        return this.fightManager;
    }

    /**
     * 初始化战斗管理器
     *
     * @param world
     */
    @Override
    public void initFightManager(World world) {
        if (!world.isRemote && world.provider instanceof DimensionChurch)
        {
            this.fightManager = ((DimensionChurch)world.provider).getFightManager();
            this.fightManager.init(this);
        }
        else
        {
            this.fightManager = null;
        }
    }

    @Override
    public int getAttackDamage(EnumAttackType type)
    {
        return factor * super.getAttackDamage(type);
    }
    /**
     * 设置攻击倍率
     *
     * @param factor
     */
    @Override
    public void setBossDamageFactor(int factor) {
        this.factor = factor;
    }

    /**
     * 得到boss的阵营，用来让对应物品识别boss阵营并操作
     *
     * @return
     */
    @Override
    public int getBossCamp() {
        return 0;
    }
}
