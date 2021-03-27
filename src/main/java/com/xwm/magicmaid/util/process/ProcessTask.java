package com.xwm.magicmaid.util.process;

import net.minecraft.entity.Entity;
import net.minecraft.util.ITickable;


public abstract class ProcessTask implements ITickable
{
    protected int priority;
    protected Entity taskOwner;
    protected int age;
    protected int maxAge;

    public ProcessTask(int priority, Entity taskOwner, int maxAge) {
        this.priority = priority;
        this.taskOwner = taskOwner;
        this.maxAge = maxAge;
    }

    @Override
    public abstract void update();

    /**
     * 任务执行是否结束
     * @return 是否结束
     */
    public boolean isFinish() {
        return age >= maxAge;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Entity getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(Entity taskOwner) {
        this.taskOwner = taskOwner;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
