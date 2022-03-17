package com.xwm.magicmaid.player.skill.perfomskill.unreachable;

import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.manager.IMagicCreatureManagerImpl;
import com.xwm.magicmaid.manager.IProcessManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.object.item.equipment.ItemEquipment;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.process.ProcessTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;
import java.util.Random;

public class PerformSkillRepentance extends PerformSkillUnreachableBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon_2.png");

    @Override
    public int getPerformEnergy() {
        return 100;
    }

    @Override
    public int getColdTime() {
        return 100;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        ItemStack stack = playerIn.getHeldItemMainhand();
        if (stack.getItem() != ItemInit.ITEM_REPANTENCE) {
            playerIn.sendMessage(new TextComponentString("必须手持[忏悔]才可以释放"));
            return;
        }

        IProcessManagerImpl.getInstance().addTask(new ProcessSkillTaskRepentance(1, playerIn, 100));
        curColdTime = getColdTime();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getRequirePoint() {
        return 0;
    }

    @Override
    public String getName() {
        return super.getName() + ".repentance";
    }

    @Override
    public String getDescription() {
        return "武器专精:忏悔";
    }


    @Override
    public String getDetailDescription() {
        return "手持忏悔，产生一个灵魂光环，阻碍怪物前进\n范围随着武器等级提升而提升";
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 40.0;
        double scaley = 46.0 / 40.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 40, 40, 40);
        GlStateManager.popMatrix();
    }

    private static class ProcessSkillTaskRepentance extends ProcessTask
    {
        private World world;
        private EntityLivingBase player;
        private Random random = new Random();

        public ProcessSkillTaskRepentance(int priority, EntityLivingBase taskOwner, int maxAge) {
            super(priority, taskOwner, maxAge);
            this.world = taskOwner.getEntityWorld();
            this.player = taskOwner;
        }

        @Override
        public void update() {

            try {
                ItemStack stack = player.getHeldItemMainhand();
                ItemEquipment equipment = (ItemEquipment) stack.getItem();
                stack.damageItem(1, player);
                int level = equipment.getLevel(stack);
                player.motionX = 0;
                player.motionY = 0;
                player.motionZ = 0;

                if (age % 20 == 0 && !world.isRemote) {
                    List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(level, level, level));
                    for (EntityLivingBase entityLivingBase : entityLivingBases) {
                        if (MagicEquipmentUtils.checkEnemy(player, entityLivingBase)) {
                            IMagicCreatureManagerImpl.getInstance().attackEntityFrom(entityLivingBase, DamageSource.causeMobDamage(player).setMagicDamage(), 1);
                            entityLivingBase.motionX = 0.25 * (entityLivingBase.posX - player.posX);
                            entityLivingBase.motionY = 0;
                            entityLivingBase.motionZ = 0.25 * (entityLivingBase.posZ - player.posZ);
                            for (int i = 0; i < 4; i++) {
                                SPacketThreeParamParticle packet = new SPacketThreeParamParticle(entityLivingBase.posX + random.nextFloat(), entityLivingBase.posY + random.nextFloat(), entityLivingBase.posZ + random.nextFloat(), EnumCustomParticles.SOUL);
                                NetworkLoader.instance.sendToAllAround(packet, new NetworkRegistry.TargetPoint(taskOwner.dimension, taskOwner.posX, taskOwner.posY, taskOwner.posZ, 20));
                            }
                        }
                    }

                    int n = level * 24;
                    int r = level;
                    double perAngle = 360.0 / n;
                    for (int j = 0; j < n; j++) {
                        SPacketThreeParamParticle packet = new SPacketThreeParamParticle(taskOwner.posX + r * Math.sin(perAngle * j), taskOwner.posY + taskOwner.height / 2, taskOwner.posZ + r * Math.cos(perAngle * j), EnumCustomParticles.SOUL);
                        NetworkLoader.instance.sendToAllAround(packet, new NetworkRegistry.TargetPoint(taskOwner.dimension, taskOwner.posX, taskOwner.posY, taskOwner.posZ, r * 4));
                    }
                }
                age++;
            } catch (Exception e) {
                this.setAge(this.getMaxAge());
            }
        }
    }
}
