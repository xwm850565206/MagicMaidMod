package com.xwm.magicmaid.event.loader;


import com.xwm.magicmaid.entity.mob.basic.AbstractEntityMagicCreature;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.event.SkillLevelUpEvent;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.init.EntityInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.init.PotionInit;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.ISkillManagerImpl;
import com.xwm.magicmaid.player.capability.*;
import com.xwm.magicmaid.player.skill.IAttributeSkill;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.registry.MagicSkillRegistry;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Random;

@Mod.EventBusSubscriber
public class CommonEventLoader
{
    private static final Random rand =  new Random();

    /**
     * 添加magic creature的capability
     * @param event
     */
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof AbstractEntityMagicCreature || event.getObject() instanceof EntityPlayer)
        {
            if (!event.getObject().hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null)) {
                ICapabilitySerializable<NBTTagCompound> provider = new CapabilityMagicCreature.Provider();
                event.addCapability(new ResourceLocation(Reference.MODID + ":" + "magic_creature"), provider);
            }
            if (event.getObject() instanceof EntityPlayer && !event.getObject().hasCapability(CapabilityLoader.SKILL_CAPABILITY, null)) {
                ICapabilitySerializable<NBTTagCompound> provider = new CapabilitySkill.Provider();
                event.addCapability(new ResourceLocation(Reference.MODID + ":" + "magic_skill"), provider);
            }
        }
    }

    /**
     * 注册实体
     * @param event
     */
    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event)
    {
        EntityInit.registerEntities(event);
    }

    /**
     * 实现逻辑martha守护者祝福为其他生物添加祝福
     * @param event
     */
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

    /**
     * 实现逻辑rett的不朽者祝福逻辑，在受到致命伤时避免伤害
     * @param event
     */
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

    /**
     * 复制 skill capability 和 creature capability
     */
    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
        {
            ISkillCapability old = event.getOriginal().getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            ISkillCapability ne = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (ne != null && old != null) {
                ne.fromSkillCapability(old);
            }
        }

        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability old = event.getOriginal().getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            ICreatureCapability ne = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (ne != null && old != null) {
                ne.fromCreatureCapability(old);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        ISkillCapability skillCapability = event.player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
        for (IAttributeSkill skill : skillCapability.getAttributeSkills()) {
            MinecraftForge.EVENT_BUS.post(new SkillLevelUpEvent.Post(skill, event.player));
        }
        ISkillManagerImpl.getInstance().updateToClient(event.player);
        IMagicCreatureManagerImpl.getInstance().updateToClient(event.player);
    }

    /**
     * 大智者的祝福逻辑，技能冷却逻辑
     * @param event
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        PotionEffect effect = player.getActivePotionEffect(PotionInit.WISE_BLESS_EFFECT);
        if (effect != null && effect.getDuration() > 1)
        {
            if (!player.capabilities.allowFlying) {
                player.capabilities.allowFlying = true;
//                player.motionY += 1.0;
//                player.capabilities.isFlying = true;
            }
        }
        else if (effect != null && effect.getDuration() == 1){
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
        }

        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
        {
            ISkillCapability skillCapability = player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            if (skillCapability != null)
            {
                for (IPerformSkill performSkill : skillCapability.getPerformSkills())
                {
                    performSkill.update();
                }
                // todo 被动技能可能也需要tick
            }
        }

        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            ICreatureCapability creatureCapability = player.getCapability(CapabilityLoader.CREATURE_CAPABILITY, null);
            if (creatureCapability != null)
            {
                creatureCapability.setEnergy(Math.min(creatureCapability.getMaxEnergy(), creatureCapability.getEnergy() + creatureCapability.getPerEnergy() * creatureCapability.getSkillSpeed()));
            }
        }
    }


    /**
     * 掉落福音书 遗失之匙的逻辑
     * @param event
     */
    @SubscribeEvent
    public static void onZombieDieEvent(LivingDeathEvent event)
    {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        if (entityLivingBase.getEntityWorld().isRemote)
            return;

        if (entityLivingBase instanceof EntityZombie)
        {
            double p = rand.nextDouble();
            if (entityLivingBase.getEntityWorld().provider.getDimension() == DimensionInit.DIMENSION_CHURCH){
                if (p < 1){
                    ItemStack stack = new ItemStack(ItemInit.ITEM_LOST_KEY, 1);
                    EntityItem entityItem = new EntityItem(entityLivingBase.getEntityWorld(), entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, stack);
                    entityLivingBase.getEntityWorld().spawnEntity(entityItem);
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

    /**
     * 实现女仆打怪加经验的逻辑
     * @param event
     */
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

    /**
     * 给终焉记事，更新技能和属性，给礼物技能
     * @param event
     */
    @SubscribeEvent
    public void onPlayerLoggin(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.getEntityWorld();
        if (!world.isRemote) {
            if (player.getHeldItemOffhand().isEmpty())
                player.setHeldItem(EnumHand.OFF_HAND, new ItemStack(ItemInit.ITEME_INSTRUCCTION_BOOK));
            else if (player.getHeldItemOffhand().getItem() != ItemInit.ITEME_INSTRUCCTION_BOOK){
                EntityItem entityItem = new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, new ItemStack(ItemInit.ITEME_INSTRUCCTION_BOOK));
                world.spawnEntity(entityItem);
            }

            ISkillCapability skillCapability = event.player.getCapability(CapabilityLoader.SKILL_CAPABILITY, null);
            for (IAttributeSkill skill : skillCapability.getAttributeSkills()) {
                MinecraftForge.EVENT_BUS.post(new SkillLevelUpEvent.Post(skill, event.player));
            }

            // todo 暂时只能给主动技能，之后可以改成根据名字判断，然后给对应的技能
            for (ModContainer modContainer : Loader.instance().getModList()) {
                String modid = modContainer.getModId();
                if (MagicSkillRegistry.GIFTED_MAP.containsKey(modid)) {
                    String giftedSkillName = MagicSkillRegistry.GIFTED_MAP.get(modid);
                    if (skillCapability.getPerformSkill(giftedSkillName) == null || skillCapability.getPerformSkill(giftedSkillName).equals(MagicSkillRegistry.PERFORM_SKILL_NONE))
                    {
                        skillCapability.setPerformSkill(giftedSkillName, (IPerformSkill) MagicSkillRegistry.getSkill(giftedSkillName));
                    }
                }
            }

            IMagicCreatureManagerImpl.getInstance().updateToClient(player);
            ISkillManagerImpl.getInstance().updateToClient(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        EntityPlayer player = event.player;
        if (player.hasCapability(CapabilityLoader.SKILL_CAPABILITY, null))
        {
           ISkillManagerImpl.getInstance().updateToClient(player);
        }

        if (player.hasCapability(CapabilityLoader.CREATURE_CAPABILITY, null))
        {
            IMagicCreatureManagerImpl.getInstance().updateToClient(player);
        }
    }


    /**
     * 掉落亡魂和执念的逻辑
     * @param event
     */
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

    /**
     * 战斗逻辑
     * todo 攻击倍率和技能倍率还没实装
     */
    @SubscribeEvent()
    public void onEntityAttacked(LivingDamageEvent event)
    {
        if (event.getEntityLiving() instanceof AbstractEntityMagicCreature)
            return;
        else {
            try {
                event.setAmount(IMagicCreatureManagerImpl.getInstance().caculateDamageAmount((EntityLivingBase) event.getSource().getImmediateSource(), event.getEntityLiving(), null, event.getAmount()));
            } catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
