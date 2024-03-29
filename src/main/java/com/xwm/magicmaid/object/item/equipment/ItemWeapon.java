package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemWeapon extends ItemEquipment
{
    public ItemWeapon(String name) {
        super(name);
        this.maxStackSize = 1;
        this.setMaxDamage(100);
    }

    public EquipmentAttribute getAttackType() {
        return getEquipmentAttribute();
    }

    public List<Integer> getAttackDamage() {
        List<Integer> attackList = new ArrayList<>();
        for (int i = 0; i <= 7; i++)
        {
            attackList.add((int) Math.ceil(Math.sqrt(getBaseDamage() * i * 7 * Math.log(1 + i * i))));
        }

        return attackList;
    }

    //基础伤害
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        int level = getLevel(stack);
        if (level < 7) {
            tooltip.add(TextFormatting.RED + "等级: " + level);
        }
        else {
            tooltip.add(TextFormatting.RED + "满级");
        }
        tooltip.add(TextFormatting.DARK_RED + "伤害: " + MagicEquipmentUtils.getAttackDamage(stack, getAttackType()));
        tooltip.add(TextFormatting.DARK_RED + "攻击范围: " + MagicEquipmentUtils.getUsingArea(stack, null, null));
        if (this.isChargeable()) {
            tooltip.add(TextFormatting.DARK_RED + "施法时间: " + this.getMaxItemUseDuration(stack));
        }
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
    public void onUse(World worldIn, EntityLivingBase playerIn, EnumHand handIn, @Nullable List<EntityLivingBase> entityLivingBases)
    {

    }

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

}
