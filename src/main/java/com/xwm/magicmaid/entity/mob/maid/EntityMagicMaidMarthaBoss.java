package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityBossCreature;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.manager.IMagicBossManager;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.RenderAreaPacket;
import com.xwm.magicmaid.network.entity.SPacketMaidInventoryUpdate;
import com.xwm.magicmaid.object.item.equipment.EquipmentAttribute;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.registry.MagicRenderRegistry;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import com.xwm.magicmaid.world.dimension.DimensionChurch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EntityMagicMaidMarthaBoss extends EntityMagicMaidMartha implements IEntityBossCreature
{
    private int factor = 1;
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);

    public EntityMagicMaidMarthaBoss(World worldIn) {
        super(worldIn);
        this.initFightManager(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.setMode(EnumMode.toInt(EnumMode.BOSS));
        this.setRank(2);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source.getDamageType().equals("killed_martha")) {
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
        super.onLivingUpdate();

        if (MagicEquipmentRegistry.getAttribute(this.getWeaponType()) == MagicEquipmentRegistry.NONE && !world.isRemote) {
            ItemStack stack = ItemStack.EMPTY;
            double f = rand.nextDouble();
            if (f < 0.5)
                stack = new ItemStack(ItemInit.ITEM_REPANTENCE);
            else
                stack = new ItemStack(ItemInit.ITEM_CONVICTION);

            this.inventory.setInventorySlotContents(0, stack);
            SPacketMaidInventoryUpdate packet = new SPacketMaidInventoryUpdate(getEntityId(), dimension, 0, stack);
            NetworkLoader.instance.sendToDimension(packet, dimension);
        }
        if (MagicEquipmentRegistry.getAttribute(this.getArmorType()) == MagicEquipmentRegistry.NONE){
            this.inventory.setInventorySlotContents(1, new ItemStack(ItemInit.ITEM_PROTECTOR));
        }

        this.bossInfo.setName(this.getDisplayName().appendText(" 剩余血量: " + getHealthBarNum()));
        this.bossInfo.setPercent(getHealth() / getMaxHealth());


        if (fightManager != null)
            fightManager.onBossUpdate(this);

    }

    @Override
    public void onDeathUpdate()
    {
        super.onDeathUpdate();
        if (getMaxHealth() > 0 && getTrueHealth() <= 0 && fightManager != null) {
            if (this.deathTime == 20) {
                fightManager.setBossAlive(false); //boss真实死亡
            }
            fightManager.setBossKilled(true);
        }
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        if (getTrueHealth() > 0) return null;
        EquipmentAttribute equipment = MagicEquipmentRegistry.getAttribute(getWeaponType());
        return equipment == MagicEquipmentRegistry.REPANTENCE ? LootTableHandler.REPANTENCE : LootTableHandler.CONVICTION;
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
        return super.attackEntityFrom(DamageSource.
                causePlayerDamage(player), getMaxHealth()+1);
    }

    /**
     * 获取战斗管理器
     *
     * @return
     */
    @Override
    public IMagicBossManager getFightManager() {
        return this.fightManager;
    }

    /**
     * 初始化战斗管理器
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
    public int getAttackDamage(EquipmentAttribute type)
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

    /**
     * 创造一个警告区域，用于boss攻击的抬手范围提示
     *
     * @param i  渲染区域的id号，必须唯一
     * @param bb 攻击区域
     */
    @Override
    public void createWarningArea(int i, AxisAlignedBB bb) {
        if (world.isRemote)
            MagicRenderRegistry.addRenderBox(i, bb);
        else
        {
            RenderAreaPacket packet = new RenderAreaPacket(i, 0, bb);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(this.getEntityWorld().provider.getDimension(), posX, posY, posZ, 40.0D);
            NetworkLoader.instance.sendToAllAround(packet, target);
        }
    }

    /**
     * 消除警告区域
     *
     * @param i 渲染区域的id号
     */
    @Override
    public void removeWarningArea(int i) {
        if (world.isRemote)
            MagicRenderRegistry.removeRenderBox(i);
        else
        {
            RenderAreaPacket packet = new RenderAreaPacket(i, 1);
            NetworkLoader.instance.sendToAll(packet);
        }
    }
    protected boolean shouldDropEquipment() {
        return false;
    }

}
