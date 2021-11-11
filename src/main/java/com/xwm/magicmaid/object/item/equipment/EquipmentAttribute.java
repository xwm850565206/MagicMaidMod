package com.xwm.magicmaid.object.item.equipment;

import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

public class EquipmentAttribute
{
    protected String name;
    protected ItemEquipment equipment;
    protected Item piece;
    protected Class<? extends Entity> entityClass;
    protected EquipmentType type;
    protected List<Integer> attackDamage;
    protected Vec3d baseArea;
    protected Vec3d growArea;

    public EquipmentAttribute()
    {
        MagicEquipmentRegistry.ATTRIBUTES_LIST.add(this);
    }

    public String getName() {
        return name;
    }

    public EquipmentAttribute setName(String name) {
        this.name = name;
        return this;
    }

    public ItemEquipment getEquipment() {
        return equipment;
    }

    public EquipmentAttribute setEquipment(ItemEquipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public Item getPiece() {
        return piece;
    }

    public EquipmentAttribute setPiece(Item piece) {
        this.piece = piece;
        return this;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public EquipmentAttribute setEntityClass(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public EquipmentType getType() {
        return type;
    }

    public EquipmentAttribute setType(EquipmentType type) {
        this.type = type;
        return this;
    }

    public List<Integer> getAttackDamage() {
        return attackDamage;
    }

    public EquipmentAttribute setAttackDamage(List<Integer> attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }

    public Vec3d getBaseArea() {
        return baseArea;
    }

    public EquipmentAttribute setBaseArea(Vec3d baseArea) {
        this.baseArea = baseArea;
        return this;
    }

    public Vec3d getGrowArea() {
        return growArea;
    }

    public EquipmentAttribute setGrowArea(Vec3d growArea) {
        this.growArea = growArea;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof EquipmentAttribute) {
            EquipmentAttribute that = (EquipmentAttribute) o;
            return name.equals(that.name);
        }
        else if (o instanceof String) {
            return name.equals(o);
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static enum EquipmentType {
        WEAPON, ARMOR, NONE
    }
}
