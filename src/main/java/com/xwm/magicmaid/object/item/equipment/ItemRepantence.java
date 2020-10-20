package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.network.CustomerParticlePacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.ParticlePacket;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class ItemRepantence extends ItemWeapon
{
    private float radius = 2.0f;
    private float perAngle = 20f;

    public ItemRepantence(String name) {
        super(name);
        enumEquipment = EnumEquipment.REPATENCE;
    }
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "修女祷告时使用的物品，听说能够镇压邪灵");
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 30;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
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
            List<EntityLiving> entityLivings = world.getEntitiesWithinAABB(EntityLiving.class, cbb.grow(radius * 3));
            for (EntityLiving entityLiving : entityLivings)
            {
                if (player instanceof EntityPlayer && !PlayerEquipmentUtils.checkEnemy((EntityPlayer) player, entityLiving))
                    continue;

                entityLiving.attackEntityFrom(new EntityDamageSource("repantence_attack", player).setDamageBypassesArmor(),
                        PlayerEquipmentUtils.getAttackDamage(player, EnumAttackType.REPANTENCE));

                playParticle(entityLiving.getEntityBoundingBox(), world);
                if (entityLiving.getHealth() <= 0)
                    playDeathParticle(entityLiving.getEntityBoundingBox(), world);

            }
        }
        super.onUsingTick(stack, player, count);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
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
                ParticlePacket particlePacket = new ParticlePacket(
                        d0 + radius * Math.sin(Math.toRadians(j * perAngle)),
                        d1 + perHeight * i,
                        d2 + radius * Math.cos(Math.toRadians(j * perAngle)), EnumParticleTypes.SMOKE_LARGE);
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), d0, d1, d2, 40.0D);
                NetworkLoader.instance.sendToAllAround(particlePacket, target);
            }
    }

    private void playDeathParticle(AxisAlignedBB bb, World world){
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = (bb.maxY);
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        for (int j = 0; j < 2; j++)
        {
            CustomerParticlePacket particlePacket = new CustomerParticlePacket(
                    d0 + itemRand.nextDouble(),
                    d1 + itemRand.nextDouble(),
                    d2 + itemRand.nextDouble(), EnumCustomParticles.SOUL);
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(world.provider.getDimension(), d0, d1, d2, 40.0D);
            NetworkLoader.instance.sendToAllAround(particlePacket, target);
        }
    }
}
