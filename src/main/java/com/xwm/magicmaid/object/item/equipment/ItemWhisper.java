package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWhisper extends ItemWeapon
{
    private static final int perAngle = 10;
    private static final int[] hexameron = new int[]{0, 60, 120, 180, 240, 300};

    private AxisAlignedBB cbb = null;


    public ItemWhisper(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "传说这把法杖的第一任主人是大法师戴安娜");
        tooltip.add(TextFormatting.YELLOW + "上面的结晶注入了她无穷的魔力");
        tooltip.add(TextFormatting.YELLOW + "可以右键使用");

        tooltip.add("");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 50 - 6 * getLevel(stack);
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
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
    public void onUse(World worldIn, EntityLivingBase playerIn, EnumHand handIn, @Nullable List<EntityLivingBase> entityLivingBases) {
        for (EntityLivingBase entityLiving1 : entityLivingBases)
        {
            try{
                if (playerIn instanceof EntityPlayer && !MagicEquipmentUtils.checkEnemy((EntityPlayer) playerIn, entityLiving1))
                    continue;
                // 造成大量伤害伤害 和 10点真实伤害
                if (playerIn instanceof EntityPlayer)
                    IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLiving1, DamageSource.causePlayerDamage((EntityPlayer) playerIn), MagicEquipmentUtils.getAttackDamage(playerIn, playerIn.getHeldItem(handIn), getEquipmentAttribute()));
                else
                    IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLiving1, DamageSource.causeMobDamage(playerIn), MagicEquipmentUtils.getAttackDamage(playerIn, playerIn.getHeldItem(handIn), getEquipmentAttribute()));

                entityLiving1.getCombatTracker().trackDamage(DamageSource.LIGHTNING_BOLT, entityLiving1.getHealth(), 0);
                IMagicCreatureManagerImpl.getInstance().setHealth(entityLiving1, entityLiving1.getHealth() - 10);
                playerIn.getEntityWorld().playEvent(3000, entityLiving1.getPosition(), 10);
                float radius = (float) MagicEquipmentUtils.getRadiusFromAxisAlignedBB(MagicEquipmentUtils.getUsingArea(playerIn.getHeldItem(handIn), null, null));
                EntityLightningBolt bolt = new EntityLightningBolt(playerIn.getEntityWorld(), entityLiving1.posX + itemRand.nextInt(2* (int) radius) - radius, cbb.maxY, entityLiving1.posZ + itemRand.nextInt(2 * (int) radius) - radius, true);
                playerIn.getEntityWorld().addWeatherEffect(bolt);
                playerIn.getEntityWorld().spawnEntity(bolt);

            }catch (Exception e){
                ;
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
        if (cbb == null) {
            Vec3d vec3d = player.getLookVec();
            EnumFacing facing = EnumFacing.getFacingFromVector((float) vec3d.x, (float)vec3d.y, (float)vec3d.z);
            BlockPos pos = player.getPosition();
            for (int i = 0; i < 20; i++) {
                EntityLiving entity = (EntityLiving) player.getEntityWorld().findNearestEntityWithinAABB(EntityLiving.class, new AxisAlignedBB(pos).grow(2), player);
                if (entity != null) {
                    cbb = entity.getEntityBoundingBox();
                    break;
                }
                pos = pos.offset(facing, 1);
            }
        }

        float radius = (float) MagicEquipmentUtils.getRadiusFromAxisAlignedBB(MagicEquipmentUtils.getUsingArea(stack, null, null));
        if (count != 0 && cbb != null && player.getEntityWorld().isRemote) {
            //画圆
            float d0 = (float) ((cbb.minX + cbb.maxX) / 2.0);
            float d1 = (float) cbb.minY;
            float d2 = (float) ((cbb.minZ + cbb.maxZ) / 2.0);
            for (int i = 0; i < 36; i++){
                ParticleSpawner.spawnParticle(EnumCustomParticles.WHISPER,
                        d0 + radius * Math.sin(Math.toRadians(i * perAngle)),
                        d1,
                        d2 + radius * Math.cos(Math.toRadians(i * perAngle)),
                        0, 0, 0);
            }
            //六芒星
            for (int i = 0; i < 2; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    int k0 = i + j*2 % 6;
                    int k1 = i + (j+1)*2 % 6;
                    float t0 = (float) (d0 + radius * Math.sin(Math.toRadians(hexameron[k0])));
                    float t1 = d1;
                    float t2 = (float) (d2 + radius * Math.cos(Math.toRadians(hexameron[k0])));

                    float t3 = (float) (d0 + radius * Math.sin(Math.toRadians(hexameron[k1])));
                    float t4 = d1;
                    float t5 = (float) (d2 + radius * Math.cos(Math.toRadians(hexameron[k1])));

                    playLinearParticle(t0, t1, t2, t3, t4, t5, player.getEntityWorld());
                }
            }
        }
        if (count == 1 && cbb != null)
        {
            List<EntityLivingBase> entityLivingBaseList = player.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, MagicEquipmentUtils.getUsingArea(stack, player, cbb));
            this.onUse(player.getEntityWorld(), player, EnumHand.MAIN_HAND, entityLivingBaseList);
            cbb = null;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (cbb == null) {
            Vec3d vec3d = playerIn.getLookVec();
            EnumFacing facing = EnumFacing.getFacingFromVector((float) vec3d.x, (float)vec3d.y, (float)vec3d.z);
            BlockPos pos = playerIn.getPosition();
            for (int i = 0; i < 20; i++) {
                EntityLiving entity = (EntityLiving) playerIn.getEntityWorld().findNearestEntityWithinAABB(EntityLiving.class, new AxisAlignedBB(pos).grow(2), playerIn);
                if (entity != null) {
                    cbb = entity.getEntityBoundingBox();
                    break;
                }
                pos = pos.offset(facing, 1);
            }
        }
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
//        System.out.println(count);
        this.onUsing(stack, player, count);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        cbb = null;
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    private void playLinearParticle(float t0, float t1, float t2, float t3, float t4, float t5, World world)
    {
        float gap = 10;
        float d0 = (t3 - t0) / gap;
        float d1 = (t1 + t4) / 2.0f;
        float d2 = (t5 - t2) / gap;

        for (int i = 0; i <= 10; i++)
        {
            ParticleSpawner.spawnParticle(EnumCustomParticles.WHISPER,
                    t0 + d0 * i,
                            d1,
                    t2 + d2 * i,
                    0, 0, 0
                    );
        }
    }

    //基础伤害
    public int getBaseDamage() {
        return 20;
    }
}
