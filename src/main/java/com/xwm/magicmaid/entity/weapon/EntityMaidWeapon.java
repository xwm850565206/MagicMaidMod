package com.xwm.magicmaid.entity.weapon;

import com.xwm.magicmaid.entity.ai.EntityAIWeaponKeepPos;
import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMaidWeapon extends EntityLiving
{
    public EntityMagicMaid maid = null;
    protected AxisAlignedBB boundBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);;

    public EntityMaidWeapon(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setNoGravity(true); // 没有引力
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (maid != null) {
            BlockPos standPos = maid.getPosition();
            standPos = standPos.add(maid.weaponStandbyPos.getX(), maid.weaponStandbyPos.getY(), maid.weaponStandbyPos.getZ());
            setPosition(standPos.getX(), standPos.getY(), standPos.getZ());
            System.out.println(getPosition().toString());
        }
    }

    @Override
    protected void initEntityAI()
    {
//        this.tasks.addTask(1, new EntityAIWeaponKeepPos(this));
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isNotColliding(){ return true; }

    @Override
    protected void collideWithEntity(Entity entityIn) { }

    @Override
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.boundBox;
    }


    public void setMaid(EntityMagicMaid maid){
        this.maid = maid;
    }
}
