package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidMartha;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaidRett;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.world.dimension.ChurchTeleporter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
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

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (target instanceof EntityMagicMaidMartha)
            target.attackEntityFrom(new DamageSource("killed_martha"), 0);
        else if (target instanceof EntityMagicMaidRett)
            target.attackEntityFrom(new DamageSource("killed_rett"), 0);
        else
            target.attackEntityFrom(new DamageSource("killed_selina"), 0);

        return true;
    }

}
