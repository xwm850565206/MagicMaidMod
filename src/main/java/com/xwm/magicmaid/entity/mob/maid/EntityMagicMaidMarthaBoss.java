package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.util.handlers.LootTableHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityMagicMaidMarthaBoss extends EntityMagicMaidMartha
{
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);

    public EntityMagicMaidMarthaBoss(World worldIn) {
        super(worldIn);
        this.setMode(EnumMode.toInt(EnumMode.BOSS));
        this.setRank(2);

    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (world.isRemote)
            return;
        if (EnumEquipment.valueOf(this.getWeaponType()) == EnumEquipment.NONE){
            double f = rand.nextDouble();
            if (f < 0.5)
                this.setInventorySlotContents(0, new ItemStack(ItemInit.itemRepantence));
            else
                this.setInventorySlotContents(0, new ItemStack(ItemInit.itemConviction));

            this.setInventorySlotContents(1, new ItemStack(ItemInit.itemProtector));
        }
    }

    @Override
    public void onDeathUpdate()
    {
        super.onDeathUpdate();
        if (getTrueHealth() > 0)
            this.bossInfo.setName(this.getDisplayName().appendText(" 剩余血条: " + getHealthBarNum()));
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
        EnumEquipment equipment = EnumEquipment.valueOf(getWeaponType());
        return equipment == EnumEquipment.REPATENCE ? LootTableHandler.REPANTENCE : LootTableHandler.CONVICTION;
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
}
