package Exercici2.Nodes;

import Exercici2.NetworkEmulator.Messages.*;
import Exercici2.NetworkEmulator.NetworkEmulator;
import Exercici2.Tools.Semaphore;

import java.util.PriorityQueue;

public class LamportNode extends BaseNode{

    private String [] neighbors = null;
    private String parent = null;

    private PriorityQueue<LamportRequest> queue = null;
    private int timestamp = 0;
    private int acknowledgementsReceived = 0;
    private Semaphore internalSemaphore = null;

    public LamportNode(String ip, String [] neighbors, String parent) {
        super(ip);
        this.neighbors = neighbors;
        this.parent = parent;
        this.queue = new PriorityQueue<>();
        this.internalSemaphore = new Semaphore();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    protected void doMainWork() {

        try{

            while(true){

                //Wait until the node is triggered by parent
                this.internalSemaphore.doWait();

                //Broadcast critical section request
                synchronized (this){

                    //Set the number of acknowledges needed
                    this.acknowledgementsReceived = this.neighbors.length;

                    //Increment current timestamp
                    this.timestamp++;

                    //Broadcast request to all nodes
                    for(String neighbor : this.neighbors){
                        LamportRequest request = new LamportRequest(this.getIp(),neighbor,timestamp);
                        NetworkEmulator.sendMessage(request);
                    }
                }

                //Wait until other nodes allows the process to enter the critical section
                this.internalSemaphore.doWait();

                for(int i = 0 ; i < 10 ; i++){
                    System.out.println("[" + this.getIp() + " - LAMPORT]: Printing line number " + i + ".");
                    Thread.sleep(1000);
                }

                //Broadcast the release message to all neighbors
                synchronized (this){
                    this.timestamp++;
                    for(String neighbor : this.neighbors){
                        LamportRelease release = new LamportRelease(this.getIp(),neighbor,this.timestamp);
                        NetworkEmulator.sendMessage(release);
                    }
                }

                System.out.println("[" + this.getIp() + " - LAMPORT]: Ending Task...");
                TaskEndedMessage outputMessage = new TaskEndedMessage(this.getIp(), parent);
                NetworkEmulator.sendMessage(outputMessage);

            }

        }catch(Exception ignore){}

    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    protected void listenIncomingRequests() {

        try{

            //Notify that the listening thread has been started
            System.out.println("[" + this.getIp() + "]: Started!");

            while(true){

                //Wait for incoming network message
                this.doNetworkWait();

                //Grab the message from the network interface
                BaseMessage input = NetworkEmulator.receiveMessage(this);

                //Depending on the kind of the message that has been received decide what to do.
                if(input != null){

                    if(input instanceof TriggerMessage) {
                        System.out.println("[" + this.getIp() + " - LAMPORT]: Starting Task...");
                        this.internalSemaphore.doSignal();
                    }else if(input instanceof LamportRequest){
                        synchronized (this){
                            this.timestamp = Math.max(((LamportRequest) input).getTimestamp(), this.timestamp++);
                            this.queue.add((LamportRequest)input);
                            LamportAcknowledge acknowledge = new LamportAcknowledge(this.getIp(),input.getSource(),this.timestamp);
                            NetworkEmulator.sendMessage(acknowledge);
                        }
                    }else if(input instanceof LamportAcknowledge){
                        synchronized (this){
                            this.timestamp = Math.max(((LamportAcknowledge) input).getTimestamp(), this.timestamp++);
                            this.acknowledgementsReceived--;
                            if(this.acknowledgementsReceived == 0){
                                if((this.queue.peek()).getSource().equals(this.getIp())){
                                    this.internalSemaphore.doSignal();
                                }
                            }
                        }
                    }else if(input instanceof LamportRelease){
                        synchronized (this){
                            this.timestamp = Math.max(((LamportRelease) input).getTimestamp(),this.timestamp++);
                            this.queue.poll();
                            if(!this.queue.isEmpty()){
                                if((this.queue.peek()).getSource().equals(this.getIp())){
                                    this.internalSemaphore.doSignal();
                                }
                            }
                        }
                    }
                }

            }

        }catch(Exception ignored){}

    }
}
