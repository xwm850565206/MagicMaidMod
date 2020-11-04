package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.effect.EffectThrowableBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemEvil extends ItemBase
{
    public ItemEvil(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.LIGHT_PURPLE + "邪恶竟然能被凝结成实物，这种东西离得越远越好");
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            EntityThrowable entityJusticeBall = new EffectThrowableBase(worldIn, playerIn);
            entityJusticeBall.rotationPitch = playerIn.rotationPitch;
            entityJusticeBall.rotationYaw = playerIn.rotationYaw;
            entityJusticeBall.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entityJusticeBall);
        }

        playerIn.addStat(StatList.getObjectUseStats(Items.SNOWBALL));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

}
