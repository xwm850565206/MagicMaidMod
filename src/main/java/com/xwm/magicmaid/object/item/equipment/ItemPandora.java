package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPandora extends ItemWeapon
{
    public ItemPandora(String name) {
        super(name);
        this.setEquipmentAttribute(MagicEquipmentRegistry.PANDORA);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "充满黑暗力量的魔盒，里面");
        tooltip.add(TextFormatting.YELLOW + "似乎拥有无尽的力量");
        tooltip.add(TextFormatting.YELLOW + "可以右键使用");

        tooltip.add("");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (handIn != EnumHand.MAIN_HAND)
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        if (worldIn.isRemote) {
            itemstack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
        }

        onUse(worldIn, playerIn, handIn, null);

        return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
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

        if (handIn != EnumHand.MAIN_HAND)
            return;
        if (worldIn.isRemote)
            return;

        ItemStack itemstack = playerIn.getHeldItem(handIn);
//        if (!(itemstack.getItem() instanceof ItemPandora))
//            return;

        itemstack.damageItem(1, playerIn);
        EntityMaidWeaponPandorasBox box = new EntityMaidWeaponPandorasBox(worldIn, itemstack);
        AxisAlignedBB bb = playerIn.getEntityBoundingBox();

        float f = 0.2f;
        if (playerIn instanceof EntityPlayer) f = ((EntityPlayer) playerIn).eyeHeight;
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = (bb.minY + f);
        double d2 = (bb.minZ + bb.maxZ) / 2.0;
        BlockPos pos = new BlockPos(d0, d1, d2);
        pos = pos.offset(playerIn.getAdjustedHorizontalFacing(), 1);
        box.setPosition(pos.getX(), pos.getY(), pos.getZ());
        float angle = (playerIn.rotationYaw / 180F) * 3.141593F; // 水平方向的角度
        float angle2 = (-playerIn.rotationPitch / 180F) * 3.141593F; // 垂直方向的仰角
        final float speed = 1.5f;
        box.motionY = speed * MathHelper.sin(angle2); // 算出三个方向上的速度,为了方便阅读我先计算的Y轴分速度
        box.motionX = speed * MathHelper.cos(angle2) * -MathHelper.sin(angle); // 根据仰角算出速度在XZ平面上的投影,再正交分解投影
        box.motionZ = speed * MathHelper.cos(angle2) * MathHelper.cos(angle);
        box.setOtherOwner(playerIn);
        worldIn.spawnEntity(box);
        itemstack.shrink(1);
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

    //基础伤害
    public static int getBaseDamage() {
        return 1;
    }
}
