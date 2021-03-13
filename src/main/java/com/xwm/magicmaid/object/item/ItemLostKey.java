package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.manager.IMagicBossManager;
import com.xwm.magicmaid.object.item.interfaces.ICanGetSkillPoint;
import com.xwm.magicmaid.world.dimension.DimensionChurch;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemLostKey extends ItemBase implements ICanGetSkillPoint
{
    public ItemLostKey(String name) {
        super(name);
        setMaxStackSize(64);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "正义和邪恶同时缠绕在");
        tooltip.add(TextFormatting.YELLOW + "这把钥匙上,为什么会这样？");
        tooltip.add("");
        tooltip.add("可以用它右键记忆铜钟，来重置boss的击杀判定(用于解决无法召唤boss的问题)");
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) {
            return EnumActionResult.PASS;
        }
        else {
            // 不在维度 不处理
            if (!(worldIn.provider instanceof DimensionChurch)) {
                return EnumActionResult.PASS;
            }

            IMagicBossManager fightManager = ((DimensionChurch) worldIn.provider).getFightManager();
            if (fightManager.getBoss() != null) {
                try {
                    fightManager.setBoss(null);
//                    IMagicCreatureManagerImpl.getInstance().setDead((IEntityAvoidThingCreature) fightManager.getBoss());
                    fightManager.setBossAlive(false);
                    fightManager.setBossKilled(false);
                } catch (Exception e) {
                    Main.logger.info("recovery failed");
                }
                ItemStack stack = player.getHeldItem(hand);
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            else {
                fightManager.setBossAlive(false);
                fightManager.setBossKilled(false);
                ItemStack stack = player.getHeldItem(hand);
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
    }

    @Override
    public int getSkillPoint(ItemStack stack, EntityPlayer player) {
        return 2;
    }
}
