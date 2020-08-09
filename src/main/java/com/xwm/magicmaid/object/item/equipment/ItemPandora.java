package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPandora extends ItemWeapon
{
    public ItemPandora(String name) {
        super(name);
        enumEquipment = EnumEquipment.PANDORA;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        else {
            EntityMaidWeaponPandorasBox pandorasBox = new EntityMaidWeaponPandorasBox(worldIn);
            pandorasBox.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
            worldIn.spawnEntity(pandorasBox);
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }

}
