package com.xwm.magicmaid.event;


import com.google.common.base.Predicate;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.init.EntityInit;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.world.dimension.ChurchTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class EventLoader
{
    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event)
    {
        EntityInit.registerEntities(event);
    }

    @SubscribeEvent
    public static void onPlayerInteractive(PlayerInteractEvent.EntityInteractSpecific event)
    {
        if (event.getHand() != EnumHand.MAIN_HAND)
            return;
        if (event.getWorld().isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();
        PotionEffect effect = player.getActivePotionEffect(PotionInit.PROTECT_BLESS_EFFECT);
        Entity target = event.getTarget();
        if (effect != null && target instanceof EntityLivingBase)
        {
            try {
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(PotionInit.PROTECT_BLESS_EFFECT, 400 + effect.getAmplifier() * 400, effect.getAmplifier()));
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 200 + effect.getAmplifier() * 400, effect.getAmplifier()));
                player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "赐与其守护者的祝福"));
            } catch (Exception e){
                ;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDieEvent(LivingHurtEvent event)
    {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        if (entityLivingBase instanceof EntityPlayer)
        {
            if (entityLivingBase.getEntityWorld().isRemote)
                return;

            if (entityLivingBase.getHealth() <= event.getAmount() && entityLivingBase.isPotionActive(PotionInit.IMMORTAL_BLESS_EFFECT)){
                entityLivingBase.setHealth(entityLivingBase.getMaxHealth());
                entityLivingBase.sendMessage(new TextComponentString(TextFormatting.YELLOW + "不朽的魔法此刻涌现!"));
                event.setAmount(0);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        if (player.isPotionActive(PotionInit.WISE_BLESS_EFFECT))
        {
            if (!player.capabilities.isFlying) {
                player.capabilities.allowFlying = true;
                player.motionY += 1.0;
                player.capabilities.isFlying = true;
            }
        }
        else if (!player.isCreative()){
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.getEntityWorld();
        if (world.isRemote)
            return;

        WorldServer oldWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(event.fromDim);

        List<EntityMagicMaid> maidList = oldWorld.getEntities(EntityMagicMaid.class, new Predicate<EntityMagicMaid>() {
            @Override
            public boolean apply(@Nullable EntityMagicMaid input) {
                if (input != null && input.getOwnerID() != null && input.getOwnerID().equals(player.getUniqueID()))
                    return true;
                return false;
//                return true;
            }
        });
        for (EntityMagicMaid maid : maidList){
            if (event.toDim == DimensionInit.DIMENSION_CHURCH)
                maid.changeDimension(event.toDim, new ChurchTeleporter(oldWorld,event.toDim, player.posX, player.posY, player.posZ));
            else
                maid.changeDimension(event.toDim, new Teleporter(oldWorld));
        }
    }
}
