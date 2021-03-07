package com.xwm.magicmaid.object.item.equipment;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketSixParamParticle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDemonKillerSword extends ItemWeapon
{
    public ItemDemonKillerSword(String name) {
        super(name);
        this.setMaxDamage(1000);
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

        int attackTime = getAttackTime(playerIn.getHeldItem(handIn));
        EntityLivingBase target = entityLivingBases.get(0);
        if (playerIn instanceof EntityPlayer) {
            if (attackTime == 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 1));
                IMagicCreatureManagerImpl.getInstance().attackEntityFrom(target, DamageSource.causePlayerDamage((EntityPlayer) playerIn),
                        MagicEquipmentUtils.getAttackDamage(playerIn, playerIn.getHeldItem(handIn), EnumAttackType.DEMONKILLER));
                this.spawnSweepParticles(playerIn.getEntityWorld(), playerIn,30);
            }
            else if (attackTime == 1) {
                target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                IMagicCreatureManagerImpl.getInstance().attackEntityFrom(target, DamageSource.causePlayerDamage((EntityPlayer) playerIn),
                        MagicEquipmentUtils.getAttackDamage(playerIn, playerIn.getHeldItem(handIn), EnumAttackType.DEMONKILLER));
                this.spawnSweepParticles(playerIn.getEntityWorld(), playerIn,330);
            }
            else if (attackTime == 2) {
//                target.setDead();
                IMagicCreatureManagerImpl.getInstance().attackEntityFrom(target, DamageSource.causePlayerDamage((EntityPlayer) playerIn),
                        MagicEquipmentUtils.getAttackDamage(playerIn, playerIn.getHeldItem(handIn), EnumAttackType.DEMONKILLER) * 40);
                this.spawnSweepParticles(playerIn.getEntityWorld(), playerIn,90);
            }
            else
                attackTime = 0;
            attackTime++;
            if (attackTime > 2)
                attackTime = 0;
            setAttackTime(playerIn.getHeldItem(handIn), attackTime);
        }
    }


    private void spawnSweepParticles(World world, EntityLivingBase entityLivingBase, double angle)
    {
        double d0 = 1 * (double)(-MathHelper.sin(entityLivingBase.rotationYaw * 0.017453292F));
        double d1 = 1 * (double)MathHelper.cos(entityLivingBase.rotationYaw * 0.017453292F);

        if (world instanceof WorldServer)
        {
            SPacketSixParamParticle packet = new SPacketSixParamParticle(
                    entityLivingBase.posX + d0,
                    entityLivingBase.posY + (double)entityLivingBase.height * 0.5D,
                    entityLivingBase.posZ + d1, 1, angle, 0, EnumCustomParticles.SWEEP);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, 40.0D);
            NetworkLoader.instance.sendToAllAround(packet, target);
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
        tooltip.add( TextFormatting.YELLOW + "拿起它，紫红色的光芒瞬间迸发");

        tooltip.add("");
        super.addInformation(stack, worldIn, tooltip, flagIn);

    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        onUse(attacker.getEntityWorld(), attacker, EnumHand.MAIN_HAND, Lists.newArrayList(target));
        stack.damageItem(1, attacker);
        return true;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    public EnumAttackType getAttackType() {
        return EnumAttackType.DEMONKILLER;
    }

    // 基础伤害
    public int getBaseDamage() {
        return 10;
    }

    // 得到砍了几刀 0 1 2 每一刀效果不同
    public int getAttackTime(ItemStack stack)
    {
        String key = Reference.MODID + "_demonkiller";
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null || !compound.hasKey(key))
            return 0;
        return compound.getInteger(key);
    }

    public void setAttackTime(ItemStack stack, int time)
    {
        String key = Reference.MODID + "_demonkiller";
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        compound.setInteger(key, time);
        stack.setTagCompound(compound);
    }
}
