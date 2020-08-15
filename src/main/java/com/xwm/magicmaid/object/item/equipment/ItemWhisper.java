package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.world.dimension.ChurchTeleporter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class ItemWhisper extends ItemWeapon
{
    public ItemWhisper(String name) {
        super(name);
        enumEquipment = EnumEquipment.WHISPER;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "传说这把法杖的第一任主人是大法师戴安娜");
        tooltip.add(TextFormatting.YELLOW + "上面的结晶注入了她无穷的魔力");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        else {
            playerIn.changeDimension(DimensionInit.DIMENSION_CHURCH, new ChurchTeleporter((WorldServer) worldIn, DimensionInit.DIMENSION_CHURCH, playerIn.posX, playerIn.posY, playerIn.posZ));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
}
