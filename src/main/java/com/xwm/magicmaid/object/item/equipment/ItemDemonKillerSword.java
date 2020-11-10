package com.xwm.magicmaid.object.item.equipment;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.util.helper.MagicEquipmentUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class ItemDemonKillerSword extends ItemWeapon
{
    private int attackTime = 0;

    public ItemDemonKillerSword(String name) {
        super(name);
        enumEquipment = EnumEquipment.DEMONKILLINGSWORD;
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

        if (entityLivingBases == null || entityLivingBases.size() <= 0)
            return;

        EntityLivingBase target = entityLivingBases.get(0);
        if (playerIn instanceof EntityPlayer) {
            if (attackTime == 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 1));
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) playerIn),
                        MagicEquipmentUtils.getAttackDamage(playerIn, EnumAttackType.DEMONKILLER));
            }
            else if (attackTime == 1) {
                target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) playerIn),
                        2 * MagicEquipmentUtils.getAttackDamage(playerIn, EnumAttackType.DEMONKILLER));
            }
            else if (attackTime == 2) {
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) playerIn),
                        4 * MagicEquipmentUtils.getAttackDamage(playerIn, EnumAttackType.DEMONKILLER));
            }
            else
                attackTime = 0;

            attackTime++;
        }
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
        tooltip.add(TextFormatting.YELLOW + "一把从教堂枯井中找到的破剑，直到骑士长蕾特");
        tooltip.add(TextFormatting.YELLOW + "拿起它，紫红色的光芒瞬间迸发");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        onUse(attacker.getEntityWorld(), attacker, EnumHand.MAIN_HAND, Lists.newArrayList(target));
//        attacker.limbSwing += 180;

        attacker.swingArm(EnumHand.MAIN_HAND);
        return true;
    }
}
