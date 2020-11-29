package com.xwm.magicmaid.store;

import com.xwm.magicmaid.util.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldDifficultyData extends WorldSavedData
{
    private static String DATA_NAME = Reference.MODID + "_world_difficulty";
    private static String worldDifficultyName = "difficulty";
    private int worldDifficulty = 2; // 0-无操作 1-踢人和清背包改成玩家立刻死亡 2-踢人和清背包

    public WorldDifficultyData(){
        super(DATA_NAME);
    }

    public WorldDifficultyData(String name) {
        super(name);
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     *
     * @param nbt
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(worldDifficultyName))
            this.worldDifficulty = nbt.getInteger(worldDifficultyName);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger(worldDifficultyName, worldDifficulty);
        return compound;
    }

    public int getWorldDifficulty() {
        return worldDifficulty;
    }

    public void setWorldDifficulty(int worldDifficulty) {
        this.worldDifficulty = worldDifficulty;
        this.markDirty();
    }

    public static WorldDifficultyData get(World world)
    {
        MapStorage storage = world.getMapStorage();
        WorldDifficultyData record = (WorldDifficultyData) storage.getOrLoadData(WorldDifficultyData.class, DATA_NAME);

        if (record == null) {
            record = new WorldDifficultyData();
            storage.setData(DATA_NAME, record);
            world.setData(DATA_NAME, record);
        }

        return record;
    }
}
