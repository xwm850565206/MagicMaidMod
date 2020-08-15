package com.xwm.magicmaid.init;

import com.xwm.magicmaid.potion.PotionImmortalBless;
import com.xwm.magicmaid.potion.PotionProtectorBless;
import com.xwm.magicmaid.potion.PotionWiseBless;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionInit
{
    public static final Potion PROTECT_BLESS_EFFECT = new PotionProtectorBless("protect_bless", false, 0xffffff, 0,0);
    public static final PotionType PROTECT_BLESS = new PotionType("protect_bless", new PotionEffect(PROTECT_BLESS_EFFECT, 1200)).setRegistryName("protect_bless");
    public static final PotionType LONG_PROTECT_BLESS = new PotionType("protect_bless", new PotionEffect(PROTECT_BLESS_EFFECT, 2400)).setRegistryName("long_protect_bless");

    public static final Potion IMMORTAL_BLESS_EFFECT = new PotionImmortalBless("immortal_bless", false, 0xffffff, 4,0);
    public static final PotionType IMMORTAL_BLESS = new PotionType("immortal_bless", new PotionEffect(PROTECT_BLESS_EFFECT, 12000)).setRegistryName("immortal_bless");
    public static final PotionType LONG_IMMORTAL_BLESS = new PotionType("immortal_bless", new PotionEffect(PROTECT_BLESS_EFFECT, 24000)).setRegistryName("long_immortal_bless");

    public static final Potion WISE_BLESS_EFFECT = new PotionWiseBless("wise_bless", false, 0xffffff, 0,1);
    public static final PotionType WISE_BLESS = new PotionType("wise_bless", new PotionEffect(PROTECT_BLESS_EFFECT, 12000)).setRegistryName("wise_bless");
    public static final PotionType LONG_WISE_BLESS = new PotionType("wise_bless", new PotionEffect(PROTECT_BLESS_EFFECT, 24000)).setRegistryName("long_wise_bless");

    public static void registerPotions()
    {
        registerPotion(PROTECT_BLESS, LONG_PROTECT_BLESS, PROTECT_BLESS_EFFECT);
        registerPotion(IMMORTAL_BLESS, LONG_IMMORTAL_BLESS, IMMORTAL_BLESS_EFFECT);
        registerPotion(WISE_BLESS, LONG_WISE_BLESS, WISE_BLESS_EFFECT);
    }

    private static void registerPotion(PotionType defaultPotion, PotionType longPotion, Potion effect)
    {
        ForgeRegistries.POTIONS.register(effect);
        ForgeRegistries.POTION_TYPES.register(defaultPotion);
        ForgeRegistries.POTION_TYPES.register(longPotion);
    }
}