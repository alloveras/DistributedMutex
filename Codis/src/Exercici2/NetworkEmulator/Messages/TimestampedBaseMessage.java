package Exercici2.NetworkEmulator.Messages;

/*
 *  File: TimestampedBaseMessage.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

public abstract class TimestampedBaseMessage extends BaseMessage implements Comparable<TimestampedBaseMessage>{

    private int timestamp = 0;

    public TimestampedBaseMessage(int messageId, String sourceNode, String destinationNode, int timestamp) {
        super(messageId, sourceNode, destinationNode);
        this.timestamp = timestamp;
    }

    public int getTimestamp(){
        return this.timestamp;
    }

    public int compareTo(TimestampedBaseMessage other){

        if(other.getTimestamp() == this.timestamp) return other.getSource().compareTo(this.getSource());
        if(other.getTimestamp() > this.timestamp) return -1;
        return 1;

    }

}
