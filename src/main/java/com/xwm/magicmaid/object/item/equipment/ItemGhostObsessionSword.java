package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class ItemGhostObsessionSword extends ItemObsessionSword
{
    private float attackDamage = 1;

    public ItemGhostObsessionSword(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public float getAttackDamage()
    {
        return this.attackDamage;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        DamageSource source = new EntityDamageSource(Reference.DAMAGE_ELIMINATE, attacker);
        target.attackEntityFrom(source, 10);
        return super.hitEntity(stack, target, attacker);
    }
}
