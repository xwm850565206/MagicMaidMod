package com.xwm.magicmaid.manager;

import com.xwm.magicmaid.util.process.ProcessTask;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class IProcessManagerImpl implements IProcessManager {

    private static IProcessManager instance = null;
    private static Comparator<ProcessTask> processTaskComparator = new Comparator<ProcessTask>() {
        @Override
        public int compare(ProcessTask o1, ProcessTask o2) {
            if (o1.getPriority() < o2.getPriority())
                return 1;
            else if (o1.getPriority() > o2.getPriority())
                return -1;
            else
                return 0;
        }
    };


    private Queue<ProcessTask> processTaskQueue = new PriorityQueue<>(processTaskComparator);
    private Queue<ProcessTask> waitTaskQueue = new LinkedBlockingQueue<>();

    @Override
    public void worldTick() {
        for (ProcessTask task : processTaskQueue) {
            task.update();
        }

        updateTaskQueue();
    }

    @Override
    public void addTask(ProcessTask processTask) {
        waitTaskQueue.add(processTask);
    }

    @Override
    public void updateTaskQueue() {
        processTaskQueue.removeIf(ProcessTask::isFinish);
        processTaskQueue.addAll(waitTaskQueue);
        waitTaskQueue.clear();
    }

    /**
     * 单例模式，用于执行一些tick操作
     * @return 返回实例
     */
    public static IProcessManager getInstance() {
        if (instance == null)
            instance = new IProcessManagerImpl();
        return instance;
    }
}
