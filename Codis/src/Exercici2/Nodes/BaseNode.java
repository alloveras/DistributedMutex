package Exercici2.Nodes;

/*
 *  File: BaseNode.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

import Exercici2.Tools.Semaphore;

public abstract class BaseNode{

    private final Semaphore semaphore = new Semaphore();

    private String ip = null;
    private Thread listenerWorker = null;
    private Thread mainWorker = null;

    public BaseNode(String ip){

        this.ip = ip;

        this.listenerWorker = new Thread(BaseNode.this::listenIncomingRequests);

        this.mainWorker = new Thread(BaseNode.this::doMainWork);

    }

    public String getIp(){
        return this.ip;
    }

    public void doNetworkWait() throws InterruptedException {
         this.semaphore.doWait();
    }

    public void doNetworkNotify(){
        semaphore.doSignal();
    }

    public void startWorkers(){
        this.listenerWorker.start();
        this.mainWorker.start();
    }

    public void joinWorkers(){
        try{
            this.listenerWorker.join();
            this.mainWorker.join();
        }catch(Exception ignored){}
    }

    protected abstract void doMainWork();

    protected abstract void listenIncomingRequests();


}
