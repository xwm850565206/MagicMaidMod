package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMartha;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemHolyFruitMartha extends ItemBase {
    public ItemHolyFruitMartha(String name) {
        super(name);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return EnumActionResult.SUCCESS;
        else if (hand == EnumHand.MAIN_HAND){
            EntityMagicMaidMartha martha = new EntityMagicMaidMartha(worldIn);
            martha.setPosition(pos.getX(), pos.getY()+1, pos.getZ());
            worldIn.spawnEntity(martha);
            player.getHeldItem(hand).shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "蒙尘而圣洁的物品，强大的魔法能量隐隐作显");
        tooltip.add(TextFormatting.YELLOW + "右键使用来召唤玛莎");
    }
}
