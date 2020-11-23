package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.helper.MagicEquipmentUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemWeapon extends ItemEquipment
{

    public ItemWeapon(String name) {
        super(name);
        this.maxStackSize = 1;
        this.setMaxDamage(100);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (isChargeable())
            playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }


    public EnumAction getItemUseAction(ItemStack stack)
    {
        if (isChargeable())
            return EnumAction.NONE;
        else
            return super.getItemUseAction(stack);
    }
    /**
     * 是否是需要蓄力的武器
     * @return
     */
    public abstract boolean isChargeable();

    /**
     * 不用蓄力的武器调用这个函数
     */
    public abstract void onUse(World worldIn, EntityLivingBase playerIn, EnumHand handIn, @Nullable List<EntityLivingBase> entityLivingBases);

    /**
     * 蓄力时调用这个函数
     * @param stack
     * @param player
     * @param count
     */
    public void onUsing(ItemStack stack, EntityLivingBase player, int count){
        this.onUse(player.getEntityWorld(), player, EnumHand.MAIN_HAND, null);
    }

    /**
     * 得到武器的使用范围，一般是攻击范围
     * @param player
     * @param bb
     * @return
     */
    public AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb)
    {
        return MagicEquipmentUtils.getUsingArea(stack, player, bb);
    }

    /**
     * How long it takes to use or consume an item
     */
    public abstract int getMaxItemUseDuration(ItemStack stack);

    public int getLevel(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        return (compound != null && compound.hasKey(Reference.MODID + "_level" )) ? compound.getInteger(Reference.MODID + "_level") : 0;
    }

    public void setLevel(ItemStack stack, int level)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        compound.setInteger(Reference.MODID + "_level", level);
        stack.setTagCompound(compound);
    }
}
