package Exercici2.NetworkEmulator.Messages;

/*
 *  File: LamportRequest.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

public class LamportRequest extends TimestampedBaseMessage {

    public LamportRequest(String sourceNode, String destinationNode, int timestamp) {
        super(BaseMessage.LAMPORT_REQUEST, sourceNode, destinationNode,timestamp);
    }

}
