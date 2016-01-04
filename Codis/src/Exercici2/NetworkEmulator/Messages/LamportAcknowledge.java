package Exercici2.NetworkEmulator.Messages;

/*
 *  File: LamportAcknowledge.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */


public class LamportAcknowledge extends TimestampedBaseMessage {

    public int timestamp = 0;

    public LamportAcknowledge(String sourceNode, String destinationNode, int timestamp) {
        super(BaseMessage.LAMPORT_ACKNOWLEDGE, sourceNode, destinationNode, timestamp);
    }

}
