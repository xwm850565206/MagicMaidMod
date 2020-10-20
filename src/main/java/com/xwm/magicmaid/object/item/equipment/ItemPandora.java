package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import java.util.List;

public class ItemPandora extends ItemWeapon
{
    public ItemPandora(String name) {
        super(name);
        enumEquipment = EnumEquipment.PANDORA;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "充满黑暗力量的魔盒，里面");
        tooltip.add(TextFormatting.YELLOW + "似乎拥有无尽的力量");
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

        EntityMaidWeaponPandorasBox box = new EntityMaidWeaponPandorasBox(worldIn);
        AxisAlignedBB bb = playerIn.getEntityBoundingBox();
        double d0 = (bb.minX + bb.maxX) / 2.0;
        double d1 = (bb.minY + playerIn.eyeHeight);
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

        return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
    }
}
