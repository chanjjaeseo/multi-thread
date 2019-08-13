package com.qcz.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 采用阻塞队列实现 生产者-消费者模型
 */
public class BlockingQueueProduceConsumeModel<T> implements ProduceConsumeModel<T>{

    private BlockingQueue<T> queue;

    public BlockingQueueProduceConsumeModel() {
        queue = new LinkedBlockingQueue<>();
    }

    public void produce(T product) {
        queue.offer(product);
    }

    public void consume() throws InterruptedException {
        System.out.println(queue.take());
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        final BlockingQueueProduceConsumeModel<Integer> consumeModel =
                new BlockingQueueProduceConsumeModel<>();
        int count = 0;
        // FIXME 这种测试可能不太严谨
        while (count <= 10) {
            final int finalCount = count;
            es.submit(new Runnable() {
                public void run() {
                    consumeModel.produce(finalCount);
                }
            });
            es.submit(new Runnable() {
                public void run() {
                    try {
                        consumeModel.consume();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            count++;
        }

    }


}
