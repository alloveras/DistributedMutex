package Exercici2.NetworkEmulator;

/*
 *  File: Exercici2.NetworkEmulator.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

import Exercici2.NetworkEmulator.Messages.BaseMessage;
import Exercici2.Nodes.BaseNode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NetworkEmulator {

    private static final int MAX_NODES = 10;

    private static class NodeDescriptor{
        public BaseNode node;
        public int receptionIndex;
        public int nodeIndex;
    }

    private static Map<String,NodeDescriptor> nodes = new HashMap<>();
    private static LinkedList<Integer> availablePositions = new LinkedList<>();
    private static LinkedList[][] network = new LinkedList[MAX_NODES][MAX_NODES];

    static{
        for(int i = 0 ; i < MAX_NODES ; i++){
            availablePositions.add(i);
            for(int j = 0 ; j < MAX_NODES ; j++){
                network[i][j] = new LinkedList<BaseMessage>();
            }
        }
    }

    public static synchronized void attatchToNetwork(BaseNode node) throws Exception{

        if(availablePositions.size() > 0){

            int nodeIndex = availablePositions.getFirst();
            availablePositions.removeFirst();
            NodeDescriptor nodeDescriptor = new NodeDescriptor();
            nodeDescriptor.nodeIndex = nodeIndex;
            nodeDescriptor.node = node;
            nodeDescriptor.receptionIndex = 0;
            nodes.put(node.getIp(),nodeDescriptor);

        }else{
            throw new Exception("The network doesn't accept more nodes.");
        }

    }

    public static synchronized void removeFromNetwork(BaseNode node) throws Exception{

        if(nodes.containsKey(node.getIp())){

            NodeDescriptor nodeDescriptor = nodes.get(node.getIp());
            nodes.remove(node.getIp());
            availablePositions.addLast(nodeDescriptor.nodeIndex);

        }else{
            throw new Exception("This node has never been attached to the network!");
        }

    }

    @SuppressWarnings("unchecked")
    public static synchronized void sendMessage(BaseMessage message) throws Exception{

        if(nodes.containsKey(message.getDestination())){

            if(nodes.containsKey(message.getSource())){

                NodeDescriptor nodeDestination = nodes.get(message.getDestination());
                NodeDescriptor nodeSource = nodes.get(message.getSource());

                network[nodeDestination.nodeIndex][nodeSource.nodeIndex].addLast(message);
                nodeDestination.node.doNetworkNotify();

            }else{
                throw new Exception("The source node doesn't exist in the network!");
            }

        }else{
            throw new Exception("The destination node couldn't be reached!");
        }

    }

    public static synchronized BaseMessage receiveMessage(BaseNode node) throws Exception{

        if(nodes.containsKey(node.getIp())){

            NodeDescriptor nodeDescriptor = nodes.get(node.getIp());
            int currentIndex = nodeDescriptor.receptionIndex;

            do{

                if(network[nodeDescriptor.nodeIndex][currentIndex].size() > 0){
                    BaseMessage msg = (BaseMessage) network[nodeDescriptor.nodeIndex][currentIndex].getFirst();
                    network[nodeDescriptor.nodeIndex][currentIndex].removeFirst();
                    nodeDescriptor.receptionIndex = (currentIndex + 1 == MAX_NODES) ? 0 : currentIndex + 1;
                    return msg;
                }else{
                    currentIndex = (currentIndex + 1 == MAX_NODES) ? 0 : currentIndex + 1;
                }

            }while(currentIndex != nodeDescriptor.receptionIndex);

            return null;

        }else{
            throw new Exception("The node where you want to read a message is no longer available on the network!");
        }

    }




}
