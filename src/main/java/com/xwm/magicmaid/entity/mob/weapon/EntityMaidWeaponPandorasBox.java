package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityMaidWeaponPandorasBox extends EntityMaidWeapon
{
    private static final DataParameter<Boolean> ISOPEN = EntityDataManager.<Boolean>createKey(EntityMaidWeaponPandorasBox.class, DataSerializers.BOOLEAN);
    private int radius = 4;
    public int tick = 0;

    public EntityMaidWeaponPandorasBox(World worldIn) {
        super(worldIn);
        enumEquipment = EnumEquipment.PANDORA;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ISOPEN, false);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote)
            return;
        if (this.maid != null){
            //todo
            if (isOpen()){
                List<EntityLivingBase> entityLivingBases = world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox().grow(radius + maid.getRank() * 2, radius, radius + maid.getRank() * 2));
                try{
                    for (EntityLivingBase entityLivingBase : entityLivingBases)
                    {
                        if (!this.maid.isEnemy(entityLivingBase))
                            continue;
                        entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this.maid),
                                this.maid.getAttackDamage(EnumAttackType.PANDORA));
                        this.maid.heal(this.maid.getAttackDamage(EnumAttackType.PANDORA)); //吸血给自己
                        //todo 黑洞粒子效果
                    }
                }catch (Exception e){
                    ; //有的生物受到攻击会出错
                }
            }
        }
        this.tick++;
        if (this.tick == 360)
            this.tick = 0;

    }

    public void setOpen(boolean isOpen)
    {
        this.dataManager.set(ISOPEN, isOpen);
    }

    public boolean isOpen(){
        return this.dataManager.get(ISOPEN);
    }
}
