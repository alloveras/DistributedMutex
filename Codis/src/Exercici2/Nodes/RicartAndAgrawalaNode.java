package Exercici2.Nodes;

import Exercici2.NetworkEmulator.Messages.*;
import Exercici2.NetworkEmulator.NetworkEmulator;
import Exercici2.Tools.Semaphore;

import java.util.PriorityQueue;

public class RicartAndAgrawalaNode extends BaseNode {

    private String [] neighbors = null;
    private String parent = null;

    private Semaphore internalSemaphore = null;
    private PriorityQueue<RicartAndAgrawalaRequest> queue = null;
    private int timestamp = 0;
    private int acknowledgementsReceived = 0;
    private int myTimestamp = 0;

    public RicartAndAgrawalaNode(String ip, String[] neighbors, String parent) {
        super(ip);
        this.parent = parent;
        this.neighbors = neighbors;
        this.internalSemaphore = new Semaphore();
        this.queue = new PriorityQueue<>();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    protected void doMainWork() {

        try{

            while(true){

                //Wait until the parent node sends the trigger message
                this.internalSemaphore.doWait();

                synchronized (this){

                    //Increment the timestamp
                    this.timestamp++;

                    this.myTimestamp = this.timestamp;

                    //Set the number of acknowledges that have to be received
                    this.acknowledgementsReceived = this.neighbors.length - 1;

                    //Send a broadcast request to all neighbors
                    for(String neighbor : this.neighbors){
                        if(!neighbor.equals(this.getIp())){
                            RicartAndAgrawalaRequest request = new RicartAndAgrawalaRequest(this.getIp(),neighbor,this.timestamp);
                            NetworkEmulator.sendMessage(request);
                        }
                    }

                }

                //Wait until the access to the critical section is granted
                this.internalSemaphore.doWait();

                for(int i = 0 ; i < 10 ; i++){
                    System.out.println("[" + this.getIp() + " - RA]: Printing line number " + i + ".");
                    Thread.sleep(1000);
                }

                //Send the pending acknowledges
                synchronized (this){
                    this.myTimestamp = Integer.MAX_VALUE;
                    while(!this.queue.isEmpty()){
                        RicartAndAgrawalaRequest request = this.queue.poll();
                        RicartAndAgrawalaAcknowledge acknowledge = new RicartAndAgrawalaAcknowledge(this.getIp(),request.getSource(),this.timestamp);
                        NetworkEmulator.sendMessage(acknowledge);
                    }
                }

                //Send the task finished message to parent node
                System.out.println("[" + this.getIp() + " - RA]: Ending Task...");
                TaskEndedMessage outputMessage = new TaskEndedMessage(this.getIp(), parent);
                NetworkEmulator.sendMessage(outputMessage);

            }

        }catch(Exception ignored){}

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

                    if(input instanceof TriggerMessage){
                        System.out.println("[" + this.getIp() + " - RA]: Starting Task...");
                        this.internalSemaphore.doSignal();
                    }else if(input instanceof RicartAndAgrawalaRequest){
                        synchronized (this){
                            this.timestamp = Math.max(((RicartAndAgrawalaRequest) input).getTimestamp(), this.timestamp++);
                            if(this.shouldRequestBeAnswered((RicartAndAgrawalaRequest)input)){
                                RicartAndAgrawalaAcknowledge acknowledge = new RicartAndAgrawalaAcknowledge(this.getIp(),input.getSource(),this.timestamp);
                                NetworkEmulator.sendMessage(acknowledge);
                            }else{
                                this.queue.add((RicartAndAgrawalaRequest)input);
                            }
                        }
                    }else if(input instanceof RicartAndAgrawalaAcknowledge){
                        synchronized (this){
                            this.timestamp = Math.max(((RicartAndAgrawalaAcknowledge) input).getTimestamp(),this.timestamp++);
                            this.acknowledgementsReceived--;
                            if(this.acknowledgementsReceived == 0){
                                this.internalSemaphore.doSignal();
                            }
                        }
                    }

                }

            }

        }catch(Exception ignored){}

    }

    private Boolean shouldRequestBeAnswered(RicartAndAgrawalaRequest input){

        return (this.myTimestamp == Integer.MAX_VALUE) || (input.getTimestamp() < this.myTimestamp) || (input.getTimestamp() == this.myTimestamp && input.getSource().compareTo(this.getIp()) < 0);

    }
}
