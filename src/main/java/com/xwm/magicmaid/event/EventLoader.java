package com.xwm.magicmaid.event;


import com.google.common.base.Predicate;
import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.init.*;
import com.xwm.magicmaid.network.AddBookPacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.world.dimension.ChurchTeleporter;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class EventLoader
{
    private static ResourceLocation SOUL = new ResourceLocation(Reference.MODID + ":textures/items/justice.png");

    private static final Random rand =  new Random();

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
        PotionEffect effect = player.getActivePotionEffect(PotionInit.WISE_BLESS_EFFECT);
        if (effect != null && effect.getDuration() > 1)
        {
            if (!player.capabilities.isFlying) {
                player.capabilities.allowFlying = true;
                player.motionY += 1.0;
                player.capabilities.isFlying = true;
            }
        }
        else if (effect != null && effect.getDuration() == 1){
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
                if (input != null && input.getOwnerID() != null && input.getOwnerID().equals(player.getUniqueID()) && EnumMode.valueOf(input.getMode()) != EnumMode.SITTING)
                    return true;
                return false;
//                return true;
            }
        });

        for (EntityMagicMaid maid : maidList){
           maid.changeDimension(event.toDim, new ChurchTeleporter(oldWorld, event.toDim, player.posX, player.posY, player.posZ));
        }
    }

    @SubscribeEvent
    public static void onZombieDieEvent(LivingDeathEvent event)
    {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        if (entityLivingBase.getEntityWorld().isRemote)
            return;

        if (entityLivingBase instanceof EntityZombie)
        {
            double p = rand.nextDouble();
            if (entityLivingBase instanceof  EntityPigZombie){
                if (entityLivingBase.getEntityWorld().provider.getDimension() == DimensionInit.DIMENSION_CHURCH){
                    if (p < 1){
                        ItemStack stack = new ItemStack(ItemInit.ITEM_LOST_KEY, 1);
                        EntityItem entityItem = new EntityItem(entityLivingBase.getEntityWorld(), entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, stack);
                        entityLivingBase.getEntityWorld().spawnEntity(entityItem);
                    }
                }
            }
            else
            {
                if (p < 0.1){
                    ItemStack stack = new ItemStack(ItemInit.ITEM_THE_GOSPELS, 1);
                    EntityItem entityItem = new EntityItem(entityLivingBase.getEntityWorld(), entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, stack);
                    entityLivingBase.getEntityWorld().spawnEntity(entityItem);
                }
            }
        }

    }

    @SubscribeEvent
    public static void getExpEvent(LivingDeathEvent event)
    {
        try{
            EntityLivingBase entityLivingBase = (EntityLivingBase) event.getSource().getImmediateSource();
            if (entityLivingBase instanceof EntityMagicMaid && !entityLivingBase.getEntityWorld().isRemote){
                ((EntityMagicMaid) entityLivingBase).plusExp();
            }
        } catch (Exception e){
            ;
        }

    }

    //todo 维度穿越考虑删除 换成把女仆携带成物品
    @SubscribeEvent
    public static void onPlayerSpawnEvent(LivingDeathEvent event){

        EntityLivingBase player = event.getEntityLiving();
        if (!(player instanceof EntityPlayer))
            return;
        World world = player.getEntityWorld();
        if (world.isRemote)
            return;
        if (world.provider.canRespawnHere())
            return;


        BlockPos pos = ((EntityPlayer) player).getBedLocation(0);
        WorldServer[] worldServers = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
        if (pos == null || pos.equals(worldServers[0].getSpawnPoint())) return;
        List<EntityMagicMaid> maidList = new ArrayList<>();

        for(WorldServer worldServer : worldServers)
        {
            List<EntityMagicMaid> maidList1 = worldServer.getEntities(EntityMagicMaid.class, new Predicate<EntityMagicMaid>() {
                @Override
                public boolean apply(@Nullable EntityMagicMaid input) {
                    if (input != null && input.getOwnerID() != null && input.getOwnerID().equals(player.getUniqueID()))
                        return true;
                    return false;
//                return true;
                }
            });
            maidList.addAll(maidList1);
        }

        for (EntityMagicMaid maid : maidList) {
            maid.changeDimension(0, new ChurchTeleporter((WorldServer) worldServers[0], 0, pos.getX(), pos.getY() + 1, pos.getZ()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onObsssionEntityDie(LivingDeathEvent event)
    {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        NBTTagCompound entityData = entityLivingBase.getEntityData();
        DamageSource source = event.getSource();

        Entity attacker = source.getImmediateSource();
        if (attacker == null || !(attacker instanceof EntityPlayer))
            return;
        Item item = ((EntityPlayer) attacker).getHeldItemMainhand() == ItemStack.EMPTY ? null : ((EntityPlayer) attacker).getHeldItemMainhand().getItem();
        if (item == null)
            return;

        World world = entityLivingBase.getEntityWorld();
        if (entityData.hasKey(Reference.MODID + "obsession") && entityData.getBoolean(Reference.MODID + "obsession"))
        {
            if (!world.isRemote) {
                EntityItem entityItem = new EntityItem(world, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, new ItemStack(ItemInit.ITEM_OBSESSION));
                world.spawnEntity(entityItem);
            }
        }
        else if (item == ItemInit.ITEM_OBSESSION_SWORD && entityData.hasKey(Reference.MODID + "obsession") && !entityData.getBoolean(Reference.MODID + "obsession")) {
            if (!world.isRemote) {
                EntityItem entityItem = new EntityItem(world, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, new ItemStack(ItemInit.ITEM_GHOST));
                world.spawnEntity(entityItem);
            }
        }
        else if (item == ItemInit.ITEM_GHOST_OBSESSION_SWORD && entityData.hasKey(Reference.MODID + "ghost") && entityData.getBoolean(Reference.MODID + "ghost"))
        {
            if (!world.isRemote) {
                EntityItem entityItem = new EntityItem(world, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, new ItemStack(ItemInit.ITEM_ELIMINATE_SOUL_NAIL));
                world.spawnEntity(entityItem);
            }
        }
    }
}
