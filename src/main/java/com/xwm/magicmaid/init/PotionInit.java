package com.xwm.magicmaid.init;

import com.xwm.magicmaid.potion.*;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionInit
{
    public static final Potion PROTECT_BLESS_EFFECT = new PotionProtectorBless("protect_bless", false, 0xffffff, -1,-1);
    public static final Potion IMMORTAL_BLESS_EFFECT = new PotionImmortalBless("immortal_bless", false, 0xffffff, -1,-1);
    public static final Potion WISE_BLESS_EFFECT = new PotionWiseBless("wise_bless", false, 0xffffff, -1,-1);
    public static final Potion BOSS_ANGRY_EFFECT = new PotionBossAngry("boss_angry", true, 0x000000, -1,-1);

    public static final Potion BIAN_FLOWER_EFFECT = new PotionBianFlower("bian_flower", false, 0x9e0000, -1,-1);
    public static final PotionType BIAN_FLOWER = new PotionType("bian_flower", new PotionEffect[]{new PotionEffect(BIAN_FLOWER_EFFECT, 1200)}).setRegistryName("bian_flower");
    public static final PotionType LONG_BIAN_FLOWER = new PotionType("bian_flower", new PotionEffect[]{new PotionEffect(BIAN_FLOWER_EFFECT, 2400)}).setRegistryName("long_bian_flower");

    public static final Potion GHOST_EFFECT = new PotionGhost("ghost", false, 0x9e0000, -1,-1);
    public static final PotionType GHOST = new PotionType("ghost", new PotionEffect[]{new PotionEffect(GHOST_EFFECT, 1200),  new PotionEffect(MobEffects.SLOWNESS, 1200)}).setRegistryName("ghost");
    public static final PotionType LONG_GHOST = new PotionType("ghost", new PotionEffect[]{new PotionEffect(GHOST_EFFECT, 2400),  new PotionEffect(MobEffects.SLOWNESS, 2400)}).setRegistryName("long_ghost");


    public static void registerPotions()
    {
        //不添加药水
        registerPotionWithoutBottle(PROTECT_BLESS_EFFECT);
        registerPotionWithoutBottle(IMMORTAL_BLESS_EFFECT);
        registerPotionWithoutBottle(WISE_BLESS_EFFECT);
        registerPotionWithoutBottle(BOSS_ANGRY_EFFECT);

        //添加药水
        registerPotion(BIAN_FLOWER, LONG_BIAN_FLOWER, BIAN_FLOWER_EFFECT);
        registerPotion(GHOST, LONG_GHOST, GHOST_EFFECT);

        //添加合成
        registerPotionMixes();
    }

    private static void registerPotionWithoutBottle(Potion effect){
        ForgeRegistries.POTIONS.register(effect);
    }

    private static void registerPotion(PotionType defaultPotion, PotionType longPotion, Potion effect)
    {
        ForgeRegistries.POTIONS.register(effect);
        ForgeRegistries.POTION_TYPES.register(defaultPotion);
        ForgeRegistries.POTION_TYPES.register(longPotion);
    }

    private static void registerPotionMixes()
    {
        PotionHelper.addMix(PotionTypes.AWKWARD, ItemInit.ITEM_BIAN_FLOWER_POLLEN, BIAN_FLOWER);
        PotionHelper.addMix(BIAN_FLOWER, Items.REDSTONE, LONG_BIAN_FLOWER);

        PotionHelper.addMix(BIAN_FLOWER, ItemInit.ITEM_OBSESSION, GHOST);
        PotionHelper.addMix(GHOST, Items.REDSTONE, LONG_GHOST);
    }
}
