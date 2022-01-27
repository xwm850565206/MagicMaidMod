package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemGhostObsessionSword extends ItemObsessionSword
{
    private float attackDamage = 1;

    public ItemGhostObsessionSword(String name) {
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
        tooltip.add(TextFormatting.YELLOW + "熔铸亡魂后的执念，拥有特殊的力量");
        tooltip.add(TextFormatting.YELLOW + "左键攻击生物时会给生物施加凋零效果并恢复生命值");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        DamageSource source = new EntityDamageSource(Reference.DAMAGE_ELIMINATE, attacker);
        target.attackEntityFrom(source, 5);
        attacker.heal(1);

        return super.hitEntity(stack, target, attacker);
    }
}
