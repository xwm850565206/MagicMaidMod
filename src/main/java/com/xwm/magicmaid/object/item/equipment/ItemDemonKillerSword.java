package com.xwm.magicmaid.object.item.equipment;

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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemDemonKillerSword extends ItemWeapon
{
    private int attackTime = 0;

    public ItemDemonKillerSword(String name) {
        super(name);
        enumEquipment = EnumEquipment.DEMONKILLINGSWORD;
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
        if (attacker instanceof EntityPlayer) {
            if (attackTime == 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 1));
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker),
                        MagicEquipmentUtils.getAttackDamage(attacker, EnumAttackType.DEMONKILLER));
            }
            else if (attackTime == 1) {
                target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker),
                        2 * MagicEquipmentUtils.getAttackDamage(attacker, EnumAttackType.DEMONKILLER));
            }
            else if (attackTime == 2) {
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker),
                        4 * MagicEquipmentUtils.getAttackDamage(attacker, EnumAttackType.DEMONKILLER));
            }
            else
                attackTime = 0;

            attackTime++;
        }

        return true;
    }
}
