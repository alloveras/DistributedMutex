package Exercici2;

/*
 *  File: Exercici2.Main.java
 *  Project: Exercici_2
 *
 *  Created by Albert Lloveras Carbonell on 26/11/15.
 *  Copyright 2015. All rights reserved.
 *
 */

import Exercici2.NetworkEmulator.Messages.TokenMessage;
import Exercici2.NetworkEmulator.NetworkEmulator;
import Exercici2.Nodes.HeavyWeighNode;
import Exercici2.Nodes.LamportNode;
import Exercici2.Nodes.RicartAndAgrawalaNode;

public class Main {

    private static String IP_HEAVY_WEIGHT_A = "192.168.1.100";
    private static String IP_HEAVY_WEIGHT_B = "192.168.1.101";

    private static String IP_LIGHT_WEIGHT_LAMPORT_A = "192.168.2.100";
    private static String IP_LIGHT_WEIGHT_LAMPORT_B = "192.168.2.101";
    private static String IP_LIGHT_WEIGHT_LAMPORT_C = "192.168.2.102";

    private static String IP_LIGHT_WEIGHT_RA_A = "192.168.3.100";
    private static String IP_LIGHT_WEIGHT_RA_B = "192.168.3.101";
    private static String IP_LIGHT_WEIGHT_RA_C = "192.168.3.102";

    public static String [] HEAVY_WEIGHT_NODES = {
            IP_HEAVY_WEIGHT_A,
            IP_HEAVY_WEIGHT_B
    };
    public static String [] LIGHT_WEIGHT_LAMPORT_NODES = {
            IP_LIGHT_WEIGHT_LAMPORT_A,
            IP_LIGHT_WEIGHT_LAMPORT_B,
            IP_LIGHT_WEIGHT_LAMPORT_C
    };
    public static String [] LIGHT_WEIGHT_RA_NODES = {
            IP_LIGHT_WEIGHT_RA_A,
            IP_LIGHT_WEIGHT_RA_B,
            IP_LIGHT_WEIGHT_RA_C
    };


    public static void main (String [] args){

        try{

            //Create two heavy weight workers
            HeavyWeighNode hwone = new HeavyWeighNode(IP_HEAVY_WEIGHT_A,LIGHT_WEIGHT_LAMPORT_NODES,HEAVY_WEIGHT_NODES);
            HeavyWeighNode hwtwo = new HeavyWeighNode(IP_HEAVY_WEIGHT_B,LIGHT_WEIGHT_RA_NODES,HEAVY_WEIGHT_NODES);

            //Create Lamport nodes
            LamportNode lamportOne = new LamportNode(IP_LIGHT_WEIGHT_LAMPORT_A,LIGHT_WEIGHT_LAMPORT_NODES,hwone.getIp());
            LamportNode lamportTwo = new LamportNode(IP_LIGHT_WEIGHT_LAMPORT_B,LIGHT_WEIGHT_LAMPORT_NODES,hwone.getIp());
            LamportNode lamportThree = new LamportNode(IP_LIGHT_WEIGHT_LAMPORT_C,LIGHT_WEIGHT_LAMPORT_NODES,hwone.getIp());

            //Create RA nodes
            RicartAndAgrawalaNode raOne = new RicartAndAgrawalaNode(IP_LIGHT_WEIGHT_RA_A,LIGHT_WEIGHT_RA_NODES,hwtwo.getIp());
            RicartAndAgrawalaNode raTwo = new RicartAndAgrawalaNode(IP_LIGHT_WEIGHT_RA_B,LIGHT_WEIGHT_RA_NODES,hwtwo.getIp());
            RicartAndAgrawalaNode raThree = new RicartAndAgrawalaNode(IP_LIGHT_WEIGHT_RA_C,LIGHT_WEIGHT_RA_NODES,hwtwo.getIp());

            NetworkEmulator.attatchToNetwork(hwone);
            NetworkEmulator.attatchToNetwork(hwtwo);
            NetworkEmulator.attatchToNetwork(lamportOne);
            NetworkEmulator.attatchToNetwork(lamportTwo);
            NetworkEmulator.attatchToNetwork(lamportThree);
            NetworkEmulator.attatchToNetwork(raOne);
            NetworkEmulator.attatchToNetwork(raTwo);
            NetworkEmulator.attatchToNetwork(raThree);

            lamportOne.startWorkers();
            lamportTwo.startWorkers();
            lamportThree.startWorkers();
            raOne.startWorkers();
            raTwo.startWorkers();
            raThree.startWorkers();
            hwone.startWorkers();
            hwtwo.startWorkers();

            System.out.println("Waiting for all nodes to be ready...");
            Thread.sleep(3000);

            TokenMessage startupToken = new TokenMessage(IP_HEAVY_WEIGHT_A,IP_HEAVY_WEIGHT_B);
            NetworkEmulator.sendMessage(startupToken);

            hwone.joinWorkers();
            hwtwo.joinWorkers();
            lamportOne.joinWorkers();
            lamportTwo.joinWorkers();
            lamportThree.joinWorkers();
            raOne.joinWorkers();
            raTwo.joinWorkers();
            raThree.joinWorkers();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

}


