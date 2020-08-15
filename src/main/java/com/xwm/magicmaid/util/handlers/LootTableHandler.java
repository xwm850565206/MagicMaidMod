package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableHandler
{
    public static final ResourceLocation REPANTENCE = LootTableList.register(new ResourceLocation(Reference.MODID, "repantence"));
    public static final ResourceLocation CONVICTION = LootTableList.register(new ResourceLocation(Reference.MODID, "conviction"));
    public static final ResourceLocation DEMONKILLER = LootTableList.register(new ResourceLocation(Reference.MODID, "demonkiller"));
    public static final ResourceLocation PANDORA = LootTableList.register(new ResourceLocation(Reference.MODID, "pandora"));
    public static final ResourceLocation WHISPER = LootTableList.register(new ResourceLocation(Reference.MODID, "whisper"));
}
