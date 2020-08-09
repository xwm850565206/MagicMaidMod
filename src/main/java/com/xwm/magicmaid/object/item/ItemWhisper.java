package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWhisper extends ItemWeapon
{
    public ItemWhisper(String name) {
        super(name);
        enumEquipment = EnumEquipment.WHISPER;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        else {
            EntityMaidWeaponWhisper whisper = new EntityMaidWeaponWhisper(worldIn);
            whisper.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
            worldIn.spawnEntity(whisper);
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
}
