package Exercici2.NetworkEmulator.Messages;

/*
 *  File: BaseMessage.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

public abstract class BaseMessage {

    protected static final int TOKEN_MESSAGE = 0;
    protected static final int TRIGGER_MESSAGE = 1;
    protected static final int LAMPORT_REQUEST = 2;
    protected static final int LAMPORT_ACKNOWLEDGE = 3;
    protected static final int LAMPORT_RELEASE = 4;
    protected static final int RA_REQUEST = 5;
    protected static final int RA_ACKNOWLEDGE = 6;
    protected static final int TASK_ENDED_MESSAGE = 7;

    protected int messageId;
    protected String sourceNode;
    protected String destinationNode;

    public BaseMessage(int messageId, String sourceNode, String destinationNode){
        this.messageId = messageId;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    public String getSource(){
        return this.sourceNode;
    }

    public String getDestination(){
        return this.destinationNode;
    }

}
