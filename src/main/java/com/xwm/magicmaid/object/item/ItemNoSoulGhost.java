package com.xwm.magicmaid.object.item;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityAvoidThingCreature;
import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityTameableCreature;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.helper.MagicCreatureUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.List;

public class ItemNoSoulGhost extends ItemBase
{
    public ItemNoSoulGhost(String name) {
        super(name);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true); // This allows the item to be marked as a metadata item.
        this.setMaxDamage(0); // This makes it so your item doesn't have the damage bar at the bottom of its icon, when "damaged" similar to the Tools.
    }

    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        int metadata = stack.getMetadata();
        if (metadata == 0) {
            tooltip.add(TextFormatting.YELLOW + "被提炼走灵魂的魂魄，是最好的容器");
            tooltip.add(TextFormatting.YELLOW + "左键来吸取宠物的灵魂");
        }

        else
        {
            NBTTagCompound compound = stack.getTagCompound();
            NBTTagCompound compound1 = compound.getCompoundTag("soul");
            ModContainer mc = Loader.instance().getIndexedModList().get(Reference.MODID);

            int entityId = compound1.getInteger("entity");
            String name = "entity." + EntityRegistry.instance().lookupModSpawn(mc, entityId).getEntityName() + ".name";
            String name1 = I18n.format(name);

            int level = compound1.getInteger("level");
            tooltip.add(TextFormatting.YELLOW + "装着灵魂的魄");
            tooltip.add(TextFormatting.LIGHT_PURPLE + name1);
            tooltip.add(TextFormatting.LIGHT_PURPLE + "level: " + level);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "_" + getMetadata(stack);
    }


    @Override
    public int getMetadata(ItemStack stack) {
        return (stack.hasTagCompound() && stack.getTagCompound().hasKey("soul") ? 1 : 0);
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
        ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(this.getRegistryName() + "_1", "inventory"));
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if  (worldIn.isRemote)
            return EnumActionResult.PASS;

        if (!stack.hasTagCompound())
            return EnumActionResult.PASS;
        else
        {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound == null || !compound.hasKey("soul"))
                return EnumActionResult.PASS;
            else
            {
                try {
                    NBTTagCompound compound1 = compound.getCompoundTag("soul");
                    if (compound1.hasKey("cold") && compound1.getInteger("cold") < 20) {
                        player.sendMessage(new TextComponentString("灵魂还未彻底吸收，请勿放出"));
                        return EnumActionResult.FAIL;
                    }

                    ModContainer mc = Loader.instance().getIndexedModList().get(Reference.MODID);
                    int entityId = compound1.getInteger("entity");
                    Entity entity = EntityRegistry.instance().lookupModSpawn(mc, entityId).newInstance(worldIn);
                    entity.readFromNBT(compound1);
                    entity.getEntityData().setBoolean(Reference.EFFECT_ABSORB, false);

                    entity.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                    worldIn.spawnEntity(entity);
                    compound.removeTag("soul");
                    return EnumActionResult.SUCCESS;
                } catch (NullPointerException e) {
                    System.out.println("寻找生成实体失败");
                    return EnumActionResult.FAIL;
                }
            }
        }
    }

    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (entity instanceof IEntityTameableCreature && entity instanceof IEntityAvoidThingCreature) {
            if (!((IEntityTameableCreature) entity).hasOwner() || !((IEntityTameableCreature) entity).getOwnerID().equals(player.getUniqueID()))
                return false;

            if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("soul"))
                return false;

            NBTTagCompound compound = new NBTTagCompound();
            entity.getEntityData().setBoolean(Reference.EFFECT_ABSORB, true);
            entity.writeToNBT(compound);
            compound.setInteger("entity", EntityRegistry.instance().lookupModSpawn(entity.getClass(), false).getModEntityId());
            if (!player.getEntityWorld().isRemote)
                MagicCreatureUtils.setDead((IEntityAvoidThingCreature) entity);
            compound.setInteger("cold", 0); //冷却
            stack.setTagInfo("soul", compound);
            if (player.getEntityWorld().isRemote) {
                for (int i = 0; i < 4; i++) {
                    ParticleSpawner.spawnParticle(EnumCustomParticles.PANDORA, entity.posX + itemRand.nextDouble() - 0.5, entity.posY + entity.height, entity.posZ + itemRand.nextDouble() - 0.5, player.posX, player.posY + player.height - 0.2, player.posZ);
                }
            }
            return true;
        }
        else
            return false;
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        int metadata = getMetadata(stack);
        if (metadata == 1)
        {
            if (!stack.hasTagCompound())
                return;
            NBTTagCompound compound = stack.getTagCompound();
            if (compound.hasKey("soul"))
            {
                NBTTagCompound compound1 = compound.getCompoundTag("soul");
                int cold = compound1.getInteger("cold");
                cold = Math.min(20, cold+1);
                compound1.setInteger("cold", cold);
            }
        }
    }
}
