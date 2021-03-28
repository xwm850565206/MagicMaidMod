package com.xwm.magicmaid.player.skill.perfomskill.secret;

import com.google.common.base.Predicate;
import com.xwm.magicmaid.event.SkillPerformEvent;
import com.xwm.magicmaid.manager.IProcessManagerImpl;
import com.xwm.magicmaid.manager.MagicEquipmentUtils;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.network.particle.SPacketThreeParamParticle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.player.skill.IPerformSkill;
import com.xwm.magicmaid.player.skill.perfomskill.PerformProcessSkillBase;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.process.ProcessTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class PerformProcessSkillMercy extends PerformProcessSkillBase
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/icon/skill_icon.png");

    @Override
    public int getMaxAge() {
        return 200;
    }

    @Override
    public int getPerformEnergy() {
        return 200;
    }

    @Override
    public int getColdTime() {
        return 200 - level * 50;
    }

    @Override
    public void perform(EntityLivingBase playerIn, World worldIn, BlockPos posIn) {
        if (curColdTime > 0) return;
        if (MinecraftForge.EVENT_BUS.post(new SkillPerformEvent<IPerformSkill>(this, playerIn, posIn))) return;
        if (!consumEnergy(playerIn, worldIn, posIn)) return;

        playerIn.swingArm(EnumHand.MAIN_HAND);
        IProcessManagerImpl.getInstance().addTask(new ProcessSkillTaskMercy(1, playerIn, this.getMaxAge(),this.getLevel(), playerIn.posX, playerIn.posY, playerIn.posZ));

        curColdTime = getColdTime();
    }

    @Override
    public int getRequirePoint() {
        return level < getMaxLevel() ? 1000*level + 500 : -1;
    }

    @Override
    public String getName() {
        return super.getName() + ".secret.mercy";
    }

    @Override
    public String getDetailDescription() {
        return "在短暂的祷告后，给予敌人慈悲\n升级提高攻击范围和伤害，降低冷却";
    }

    @Override
    public void drawIcon(float x, float y, float scale) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        double scalex = 46.0 / 46.0;
        double scaley = 46.0 / 46.0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 90);
        GlStateManager.scale(scalex, scaley, 1);
        GlStateManager.scale(scale, scale, 1);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 87, 188, 46, 46);
        GlStateManager.popMatrix();
    }

    private static class ProcessSkillTaskMercy extends ProcessTask
    {
        private int performRadus = 2;
        private int performHeight = 4;
        private int skillLevel;
        private double x;
        private double y;
        private double z;
        private World world;
        private List<Entity> targets;

        public ProcessSkillTaskMercy(int priority, EntityLivingBase taskOwner, int maxAge, int skillLevel, double x, double y, double z) {
            super(priority, taskOwner, maxAge);
            this.skillLevel = skillLevel;
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = taskOwner.getEntityWorld();
        }

        public void update() {

//            if (taskOwner == null)
//                return;

            if (world.isRemote)
                return;

//            if (!taskOwner.isEntityAlive())
//                return;

//            x = taskOwner.posX;
//            y = taskOwner.posY;
//            z = taskOwner.posZ;


            if (age < 100 && age % 5 == 0) {
                float progress = age / 100.0f;
                double d0 = x;
                double d1 = y + performHeight * progress;
                double d2 = z;
                int num = (int) (12 * (1 - progress));
                for (int i = 0; i < num; i++) {
                    SPacketThreeParamParticle particlePacket = new SPacketThreeParamParticle(
                            d0 + (1 - progress) * performRadus * Math.sin(Math.toRadians(360 / num * i)),
                            d1 + i * 0.1,
                            d2 + (1 - progress) * performRadus * Math.cos(Math.toRadians(360 / num * i)), EnumCustomParticles.SOUL);
                    NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(taskOwner.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                    NetworkLoader.instance.sendToAllAround(particlePacket, target);
                }
            }

            else if (age == 100) {
                AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x, y, z);
                targets = world.getEntitiesWithinAABB(Entity.class, bb.grow(skillLevel * 3, 2, skillLevel * 3), new Predicate<Entity>() {
                    @Override
                    public boolean apply(@Nullable Entity input) {
                        if (input instanceof EntityLivingBase) {
                            return MagicEquipmentUtils.checkEnemy((EntityLivingBase) taskOwner, (EntityLivingBase) input);
                        }
                        return true;
                    }
                });
            }

            else if (age < getMaxAge() - 1 && age % 5 == 0){
                float progress = (age - 100.0f) / 100.0f;
                for (Entity entity : targets) {
                    if (entity != null && entity.getDistance(x, y, z) < skillLevel * 4 && entity.isEntityAlive())
                        spawnParticleLineToEntity(entity, progress);
                }
            }
            else if (age == getMaxAge() - 1 && targets != null){
                for (Entity entity : targets) {
                    if (entity.getDistance(x, y, z) < skillLevel * 4)
                    {
                        try {
                            entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase) taskOwner), skillLevel * 7);
                        } catch (Exception e) {
                            ;
                        }
                    }
                }
            }
//            System.out.println(age);
            age++;
        }

        private void spawnParticleLineToEntity(Entity entity, float progress) {
            double d0 = x;
            double d1 = y + performHeight;
            double d2 = z;
            double f0 = entity.posX;
            double f1 = entity.posY + entity.height;
            double f2 = entity.posZ;

            for (double p = 0; p < progress; p += 0.05) {
                SPacketThreeParamParticle particlePacket = new SPacketThreeParamParticle(
                        d0 + (f0-d0)*p,
                        d1 + (f1-d1)*p,
                        d2 + (f2-d2)*p, EnumCustomParticles.SOUL);
                NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(taskOwner.getEntityWorld().provider.getDimension(), d0, d1, d2, 40.0D);
                NetworkLoader.instance.sendToAllAround(particlePacket, target);
            }
        }
    }
}
