package Exercici2.NetworkEmulator.Messages;

/*
 *  File: TriggerMessage.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

public class TriggerMessage extends BaseMessage {

    public TriggerMessage(String sourceNode, String destinationNode) {
        super(BaseMessage.TRIGGER_MESSAGE, sourceNode, destinationNode);
    }

}
