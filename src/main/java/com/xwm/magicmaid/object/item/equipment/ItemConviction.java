package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class ItemConviction extends ItemWeapon
{
    public ItemConviction(String name) {
        super(name);
        enumEquipment = EnumEquipment.CONVICTION;
    }

    /**
     * 是否是需要蓄力的武器
     *
     * @return
     */
    @Override
    public boolean isChargeable() {
        return false;
    }

    /**
     * 不用蓄力的武器调用这个函数
     *
     * @param worldIn
     * @param playerIn
     * @param handIn
     */
    @Override
    public void onUse(World worldIn, EntityLivingBase playerIn, EnumHand handIn, @Nullable List<EntityLivingBase> entityLivingBases) {
        //todo
    }


    /**
     * 蓄力时调用这个函数
     *
     * @param stack
     * @param player
     * @param count
     */
    @Override
    public void onUsing(ItemStack stack, EntityLivingBase player, int count) {
        //todo
    }

    /**
     * How long it takes to use or consume an item
     *
     * @param stack
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "教堂的圣器，传说是大主教镇压恶鬼修斯时");
        tooltip.add(TextFormatting.YELLOW + "所用的武器，光是靠近它就让人心生敬畏");
    }
}
