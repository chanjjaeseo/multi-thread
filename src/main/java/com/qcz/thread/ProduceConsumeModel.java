package com.qcz.thread;

public interface ProduceConsumeModel<T> {

    void produce(T product) throws Exception;

    void consume() throws Exception;

}
