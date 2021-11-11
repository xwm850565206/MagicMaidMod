package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketParticle;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRepantence extends ItemWeapon
{
    private float radius = 2.0f;
    private float perAngle = 20f;

    public ItemRepantence(String name) {
        super(name);
    }

    /**
     * 是否是需要蓄力的武器
     *
     * @return
     */
    @Override
    public boolean isChargeable() {
        return true;
    }

    /**
     * 不用蓄力的武器调用这个函数
     *
     * @param worldIn
     * @param playerIn
     * @param handIn
     */
    @Override
    public void onUse(World worldIn, EntityLivingBase playerIn, EnumHand handIn, @Nullable List<EntityLivingBase> entityLivingBases)
    {
        if (entityLivingBases == null)
            return;

        for (EntityLivingBase entityLiving : entityLivingBases)
        {
            if (playerIn instanceof EntityPlayer && !MagicEquipmentUtils.checkEnemy((EntityPlayer) playerIn, entityLiving))
                continue;

            try {
                IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLiving, new EntityDamageSource("repantence_attack", playerIn).setDamageBypassesArmor(),
                        MagicEquipmentUtils.getAttackDamage(playerIn, playerIn.getHeldItem(handIn), getEquipmentAttribute()));

                playParticle(entityLiving.getEntityBoundingBox(), worldIn);
                if (entityLiving.getHealth() <= 0)
                    playDeathParticle(entityLiving.getEntityBoundingBox(), worldIn);
            } catch (Exception e){
                continue;
            }

        }

        if (playerIn instanceof EntityPlayer) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            stack.damageItem(1, playerIn);
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
        World world = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        AxisAlignedBB cbb = player.getEntityBoundingBox();
        if (count != 1 && (count % 5) == 0 && world.isRemote)
        {
            //画圆
            float d0 = (float) ((cbb.minX + cbb.maxX) / 2.0);
            float d1 = (float) cbb.minY;
            float d2 = (float) ((cbb.minZ + cbb.maxZ) / 2.0);
            for (int i = 0; i < 18; i++){
                ParticleSpawner.spawnParticle(EnumCustomParticles.SOUL,
                        d0 + radius * Math.sin(Math.toRadians(i * perAngle)),
                        d1 + count / 5 * 0.5,
                        d2 + radius * Math.cos(Math.toRadians(i * perAngle)),
                        0, 0, 0);
            }
        }
        else if (count == 1)
        {
            List<EntityLivingBase> entityLivings = world.getEntitiesWithinAABB(EntityLiving.class, MagicEquipmentUtils.getUsingArea(stack, player, cbb));
            this.onUse(world, player, EnumHand.MAIN_HAND, entityLivings);
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "修女祷告时使用的物品，听说能够镇压邪灵");
        tooltip.add(TextFormatting.YELLOW + "可以右键使用");

        tooltip.add("");
        super.addInformation(stack, worldIn, tooltip, flagIn);

    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return Math.max(30 - 4 * getLevel(stack), 5);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
       this.onUsing(stack, player, count);
       super.onUsingTick(stack, player, count);
    }

    private void playParticle(AxisAlignedBB bb, World world){

        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = bb.minY;
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        double radius = Math.max((bb.maxX - bb.minX), (bb.maxZ - bb.minZ)) / 2.0 + 0.25;
        double perAngle = 360 / 10.0;
        double perHeight = (bb.maxY - bb.minY) / 6.0;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 10; j++)
            {
                SPacketParticle sPacketParticle = new SPacketParticle(
                        d0 + radius * Math.sin(Math.toRadians(j * perAngle)),
                        d1 + perHeight * i,
                        d2 + radius * Math.cos(Math.toRadians(j * perAngle)), EnumParticleTypes.SMOKE_LARGE);
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), d0, d1, d2, 40.0D);
                NetworkLoader.instance.sendToAllAround(sPacketParticle, target);
            }
    }

    private void playDeathParticle(AxisAlignedBB bb, World world){
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = (bb.maxY);
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        for (int j = 0; j < 2; j++)
        {
            SPacketThreeParamParticle particlePacket = new SPacketThreeParamParticle(
                    d0 + itemRand.nextDouble(),
                    d1 + itemRand.nextDouble(),
                    d2 + itemRand.nextDouble(), EnumCustomParticles.SOUL);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }

    //基础伤害
    public int getBaseDamage() {
        return 15;
    }
}
