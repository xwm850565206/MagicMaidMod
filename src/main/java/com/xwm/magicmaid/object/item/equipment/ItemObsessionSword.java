package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.object.item.ItemBase;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemObsessionSword extends ItemBase
{
    private float attackDamage = 1;

    public ItemObsessionSword(String name) {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(1000);
    }

    public float getAttackDamage()
    {

        return this.attackDamage;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.YELLOW + "注入执念的铁剑，拥有特殊的力量");
        tooltip.add(TextFormatting.YELLOW + "左键攻击生物时会给生物施加凋零效果");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        DamageSource source = new EntityDamageSource(Reference.DAMAGE_OBSESSION, attacker);
        target.attackEntityFrom(source, 10);
        target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 40, 0));
        stack.damageItem(1, attacker);
        return true;
    }
}
