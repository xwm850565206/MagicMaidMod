package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.util.process.ProcessTask;

public interface IProcessManager
{
    /**
     * 世界tick
     */
    void worldTick();

    /**
     * 增加任务
     * @param processTask
     */
    void addTask(ProcessTask processTask);

    /**
     * 更新任务队列(age达到一定的任务就要结束了)
     */
    void updateTaskQueue();
}
