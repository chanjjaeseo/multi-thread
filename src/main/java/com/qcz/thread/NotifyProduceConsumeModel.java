package com.qcz.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 采用线程间通信实现 生产者-消费者模型
 */
public class NotifyProduceConsumeModel<T> implements ProduceConsumeModel<T> {

    private static final Object lock = new Object();

    private static final int max = 999;

    private List<T> list = new LinkedList<>();

    private static int count = 0;


    @Override
    public void produce(T product) throws Exception {
        synchronized (lock) {
            while (count>= max) {
                lock.wait();
            }
            count++;
            list.add(product);
            System.out.println("produce : " + product);
            lock.notifyAll();
        }
    }

    @Override
    public void consume() throws Exception {
        synchronized (lock) {
            while (count == 0) {
                lock.wait();
            }
            System.out.println("consume : "+ list.remove(list.size()-1));
            count--;
            lock.notifyAll();
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(10);
        NotifyProduceConsumeModel model = new NotifyProduceConsumeModel();
        es.submit(()->{
            for (int i = 0; i< 100; i++) {
                try {
                    model.produce(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        es.submit(()->{
            for(int i=0; i<100; i++) {
                try {
                    model.consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
