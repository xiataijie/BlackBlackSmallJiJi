package com.highcoldcoder.blackblacksmalljiji.imdemo.im4.server.thread;

import java.util.LinkedList;

public class ThreadPool extends ThreadGroup{


    private boolean isClosed = false;
    private LinkedList<Runnable> workQueue;
    private static int threadPoolID;
    private int threadID;

    public ThreadPool(int poolSize) {
        super("ThreadPool-" + (threadPoolID++));
        setDaemon(true);
        workQueue = new LinkedList<Runnable>();
        for (int i = 0; i < poolSize; i++) {
            new WorkThread().start();
        }
    }

    /**
     * 添加任务,并通知线程执行
     */
    public synchronized void addTask(Runnable task) {
        if (isClosed) {
            throw new IllegalStateException();
        }
        if (task != null) {
            System.out.println("队列有任务，通知线程消耗...");
            workQueue.add(task);
            notify();
        }
    }

    /**
     * 获取任务，从队列删除;如果队列为空，通知线程等待
     */
    public synchronized Runnable getTask() throws InterruptedException {
        while (workQueue.size() == 0) {
            if (isClosed) {
                return null;
            }
            System.out.println("队列为空，线程等待中...");
            wait();
        }
        return workQueue.removeFirst();
    }

    /**
     * 关闭线程池，清空队列
     */
    public synchronized void close() {
        if (isClosed) {
            isClosed = true;
            workQueue.clear();
            interrupt();
        }
    }

    public void join() {
        synchronized (this) {
            isClosed = true;
            notifyAll();
        }

        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for (int i = 0; i < count; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class WorkThread extends Thread {

        public WorkThread() {
            super(ThreadPool.this, "WorkThread-" + (threadID++));
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                Runnable task = null;
                try {
                    task = getTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (task == null) {
                    return;
                }

                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
