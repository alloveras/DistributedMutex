package Exercici2.NetworkEmulator.Messages;

/*
 *  File: TokenMessage.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

public class TokenMessage extends BaseMessage{

    public TokenMessage(String source, String destination) {
        super(BaseMessage.TOKEN_MESSAGE,source,destination);
    }

}
