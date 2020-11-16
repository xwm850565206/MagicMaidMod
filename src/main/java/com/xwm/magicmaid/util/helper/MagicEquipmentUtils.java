package com.xwm.magicmaid.util.helper;

import com.xwm.magicmaid.entity.mob.basic.EntityTameableCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
     * @param pos
     * @return
     */
    public static AxisAlignedBB getUsingArea(ItemStack stack, EntityLivingBase player, AxisAlignedBB bb)
    {
        if (player instanceof IEntityEquipmentCreature)
        {
            //todo
            return ((IEntityEquipmentCreature) player).getUsingArea(stack, player, bb);
        }
        else
        {
            //todo
            return player.getEntityBoundingBox().grow(1, 1, 1);
        }
    }
}
