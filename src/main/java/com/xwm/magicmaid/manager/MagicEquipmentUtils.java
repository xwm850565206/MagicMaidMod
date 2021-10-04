package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityEquipmentCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.equipment.ItemWeapon;
import com.xwm.magicmaid.player.MagicCreatureAttributes;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MagicEquipmentUtils
{
    /**
     * 检查目标是否是发起者的敌人
     * @param player 发起者
     * @param entityLivingBase 目标
     * @return 是否是敌人
     */
    public static boolean checkEnemy(EntityLivingBase player, EntityLivingBase entityLivingBase)
    {
        if (!(player instanceof EntityPlayer)) {
            if (player == entityLivingBase)
                return false;
            else if (player instanceof IEntityTameableCreature)
                if(((IEntityTameableCreature) player).hasOwner())
                {
                    if (((IEntityTameableCreature) player).getOwnerID().equals(entityLivingBase.getUniqueID())) // 主人ID
                        return false;
                    else if (entityLivingBase instanceof EntityTameable && ((IEntityTameableCreature) player).getOwnerID().equals(((EntityTameable) entityLivingBase).getOwnerId())) // 主人的宠物ID
                        return false;
                }
            return true;
        }

        if (entityLivingBase instanceof EntityMaidWeapon)
            return false;

        if (entityLivingBase == player)
            return false;

        if (player != null && entityLivingBase != null)
            if(player.getTeam() != null && entityLivingBase.getTeam() != null)
                if(player.getTeam().equals(entityLivingBase.getTeam()))
                    return false;

        if (entityLivingBase instanceof EntityTameable)
            if (((EntityTameable) entityLivingBase).getOwnerId() != null)
                if (((EntityTameable) entityLivingBase).getOwnerId().equals(player.getUniqueID()))
                    return false;

        if (entityLivingBase instanceof IEntityTameableCreature)
            if (((IEntityTameableCreature) entityLivingBase).getOwnerID() != null)
                if (((IEntityTameableCreature) entityLivingBase).getOwnerID().equals(player.getUniqueID()))
                    return false;

        return true;
    }

    public static int getAttackDamage(ItemStack stack, EnumAttackType type)
    {
        ItemWeapon weapon = (ItemWeapon) stack.getItem();
        int level = weapon.getLevel(stack);

        int damage = MagicEquipmentRegistry.getEquipmentAttack(type, level);

        return weapon.getBaseDamage() + damage;//todo 还有很多没写进来
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

        int damage = MagicEquipmentRegistry.getEquipmentAttack(type, level);

        return weapon.getBaseDamage() + damage;//todo 还有很多没写进来
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
    public static AxisAlignedBB getUsingArea(ItemStack stack, Entity player, AxisAlignedBB bb)
    {
        if (player instanceof IEntityEquipmentCreature)
        {
            return ((IEntityEquipmentCreature) player).getUsingArea(stack, (EntityLivingBase) player, bb);
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

    public static double getNormalDamageRate(ItemStack stack)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        if (compound.hasKey(MagicCreatureAttributes.NORMAL_DAMAGE_RATE.getName()))
            return compound.getDouble(MagicCreatureAttributes.NORMAL_DAMAGE_RATE.getName());
        else
            return 0;
    }

    public static void setNormalDamageRate(ItemStack stack, double normalDamageRate)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        compound.setDouble(MagicCreatureAttributes.NORMAL_DAMAGE_RATE.getName(), normalDamageRate);
    }

    public static double getSkillDamageRate(ItemStack stack)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        if (compound.hasKey(MagicCreatureAttributes.SKILL_DAMAGE_RATE.getName()))
            return compound.getDouble(MagicCreatureAttributes.SKILL_DAMAGE_RATE.getName());
        else
            return 0;
    }

    public static void setSkillDamageRate(ItemStack stack, double skillDamageRate)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        compound.setDouble(MagicCreatureAttributes.SKILL_DAMAGE_RATE.getName(), skillDamageRate);
    }

    public static double getSkillSpeed(ItemStack stack)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        if (compound.hasKey(MagicCreatureAttributes.SKILL_SPEED.getName()))
            return compound.getDouble(MagicCreatureAttributes.SKILL_SPEED.getName());
        else
            return 0;
    }

    public static void setSkillSpeed(ItemStack stack, int skillSpeed)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        compound.setDouble(MagicCreatureAttributes.SKILL_SPEED.getName(), skillSpeed);
    }

    public static double getInjuryReduction(ItemStack stack)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        if (compound.hasKey(MagicCreatureAttributes.INJURY_REDUCTION.getName()))
            return compound.getDouble(MagicCreatureAttributes.INJURY_REDUCTION.getName());
        else
            return 0;
    }

    public static void setInjuryReduction(ItemStack stack, double injuryReduction)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        compound.setDouble(MagicCreatureAttributes.INJURY_REDUCTION.getName(), injuryReduction);
    }

    public static double getIgnoreReduction(ItemStack stack)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        if (compound.hasKey(MagicCreatureAttributes.IGNORE_REDUCTION.getName()))
            return compound.getDouble(MagicCreatureAttributes.IGNORE_REDUCTION.getName());
        else
            return 0;
    }

    public static void setIgnoreReduction(ItemStack stack, double ignoreReduction)
    {
        NBTTagCompound compound = stack.getOrCreateSubCompound(Reference.MODID + "_item");
        compound.setDouble(MagicCreatureAttributes.IGNORE_REDUCTION.getName(), ignoreReduction);
    }
}
