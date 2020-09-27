package com.xwm.magicmaid.entity.mob.basic.interfaces;

import java.util.UUID;

public interface IEntityTameableCreature
{
    void setOwnerID(UUID uuid);

    UUID getOwnerID();

    boolean hasOwner();

    boolean isSitting();

    void setSitting(boolean sitting);

}
