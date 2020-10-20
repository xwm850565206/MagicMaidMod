package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.basic.EntityTameableCreature;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

public class PlayerEquipmentUtils
{
    public static boolean checkEnemy(EntityLivingBase player, EntityLivingBase entityLivingBase)
    {
        if (!(player instanceof EntityPlayer))
            return true;

        if (entityLivingBase instanceof EntityMaidWeapon)
            return false;

        if (entityLivingBase == player)
            return false;

        if (entityLivingBase instanceof EntityTameable)
            if (((EntityTameable) entityLivingBase).getOwnerId() != null)
                if (((EntityTameable) entityLivingBase).getOwnerId().equals(player.getUniqueID()))
                    return false;

        if (entityLivingBase instanceof EntityTameableCreature)
            if (((EntityTameableCreature) entityLivingBase).getOwnerID() != null)
                if (((EntityTameableCreature) entityLivingBase).getOwnerID().equals(player.getUniqueID()))
                    return false;

        return true;
    }

    public static int getAttackDamage(EntityLivingBase player, EnumAttackType type)
    {
        if (!(player instanceof EntityPlayer))
            return 10;

        int factor = 1;

        //todo 力量buff （还未测试）
        Potion potion = Potion.getPotionById(5);
        if (potion != null) {
            PotionEffect effect = player.getActivePotionEffect(potion);
            if (effect != null)
                factor = 1 + effect.getAmplifier();
        }

        int damage = 0;
        switch (type) {
            case REPANTENCE:
                damage = 10;
                break;
            case WHISPER:
                damage = 20;
                break;
            case DEMONKILLER:
                damage = 10;
                break;
            case PANDORA:
                damage = 1;
        }

        return damage * factor; //todo 还有很多没写进来
    }
}
