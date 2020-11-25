package com.xwm.magicmaid.util.helper;

import com.xwm.magicmaid.entity.mob.basic.EntityTameableCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MagicEquipmentUtils
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

    public static int getAttackDamage(ItemStack stack, EnumAttackType type)
    {
        ItemWeapon weapon = (ItemWeapon) stack.getItem();
        int level = weapon.getLevel(stack);

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
                break;
            case CONVICTION:
                damage = 50;
        }

        return damage + (int)(Math.pow(Math.log(damage * level + 1), 2));//todo 还有很多没写进来
    }

    public static int getAttackDamage(EntityLivingBase player, ItemStack stack, EnumAttackType type)
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

        ItemWeapon weapon = (ItemWeapon) stack.getItem();
        int level = weapon.getLevel(stack);

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
                break;
            case CONVICTION:
                damage = 50;
        }

        return damage * factor + (int)(Math.pow(damage * level * 0.2, 2));//todo 还有很多没写进来
    }

    public static void dropEquipment(int equipment, int count, World world, BlockPos pos)
    {
        if (world.isRemote)
            return;
        EnumEquipment enumEquipment = EnumEquipment.valueOf(equipment);
        Item item = MagicEquipmentRegistry.getPiece(enumEquipment);
        if (item != null)
        {
            EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
            world.spawnEntity(entityItem);
        }
    }

    /**
     * 得到武器的使用范围，一般是攻击范围
     * @param stack
     * @param player
     * @return
     */
    public static AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb)
    {
        if (player instanceof IEntityEquipmentCreature)
        {
            return ((IEntityEquipmentCreature) player).getUsingArea(stack, player, bb);
        }
        else
        {
            AxisAlignedBB originBB;
            if (bb != null)
                originBB = bb;
            else
                originBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

            if (!(stack.getItem() instanceof ItemWeapon))
                return originBB;

            ItemWeapon weapon = (ItemWeapon) stack.getItem();
            EnumEquipment enumEquipment = weapon.enumEquipment;
            int level = weapon.getLevel(stack);
            switch (enumEquipment){
                case NONE:
                    break;
                case PANDORA:
                    originBB = originBB.grow(4 + 0.5 * level, 4 +  0.5 * level, 4 +  0.5 * level); break;
                case WHISPER:
                    originBB = originBB.grow(4 + 0.75 * level, 2, 4 + 0.75 * level); break;
                case REPATENCE:
                    originBB = originBB.grow(6 + 0.5 * level, 6 + 0.5 * level, 6 + 0.5 * level); break;
                case CONVICTION:
                    originBB = originBB.grow(4 + level, 3, 4 + level); break;
                case DEMONKILLINGSWORD:
                    originBB = originBB.grow(1);

            }

            return originBB;
        }
    }

    public static double getRadiusFromAxisAlignedBB(AxisAlignedBB bb)
    {
        double d0 = bb.maxX - bb.minX;
        double d2 = bb.maxZ - bb.minZ;
        return (d0 + d2) / 2.0;
    }

    public static boolean attackEntityFrom(EntityLivingBase entityLivingBase, DamageSource source, float amount)
    {
        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidDamage(-1);
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidSetHealth(-1);
        }
        boolean flag = false;
        try {
            flag = entityLivingBase.attackEntityFrom(source, amount);
        }
        catch (Exception e) {
            return false;
         }
        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidDamage(50);
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidSetHealth(50);
        }

        return flag;
    }

    public static void setHealth(EntityLivingBase entityLivingBase, float amount)
    {
        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidDamage(-1);
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidSetHealth(-1);
        }

        try {
            entityLivingBase.setHealth(amount);
        }
        catch (Exception e) {
            ;
        }

        if (entityLivingBase instanceof IEntityAvoidThingCreature) {
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidDamage(50);
            ((IEntityAvoidThingCreature) entityLivingBase).setAvoidSetHealth(50);
        }
    }
}
