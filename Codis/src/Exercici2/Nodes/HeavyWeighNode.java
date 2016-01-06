package Exercici2.Nodes;

import Exercici2.NetworkEmulator.Messages.BaseMessage;
import Exercici2.NetworkEmulator.Messages.TaskEndedMessage;
import Exercici2.NetworkEmulator.Messages.TokenMessage;
import Exercici2.NetworkEmulator.Messages.TriggerMessage;
import Exercici2.NetworkEmulator.NetworkEmulator;

public class HeavyWeighNode extends BaseNode {

    private String [] children;
    private String [] neighbours;
    private int status = 0;
    private int acknowledgements = 0;

    public HeavyWeighNode(String ip, String [] children, String [] neighbours) {
        super(ip);
        this.neighbours = neighbours;
        this.children = children;
        this.status = 0;
    }

    @Override
    protected void doMainWork() {

    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    protected void listenIncomingRequests() {

        try{

            //Notify that the listening thread has been started
            System.out.println("[" + this.getIp() + "]: Started!");

            while(true) {

                //Wait for incoming network message
                this.doNetworkWait();

                //Grab the message from the network interface
                BaseMessage input = NetworkEmulator.receiveMessage(this);

                if(input instanceof TokenMessage){
                    Thread.sleep(1000);
                }

                if (input != null) {

                    switch (this.status) {

                        case 0:

                            //Check if token message has been received and send trigger to children nodes.
                            if (input instanceof TokenMessage) {
                                System.out.println("[" + this.getIp() + "]: Token message received.");
                                for (String childNode : this.children) {
                                    TriggerMessage trigger = new TriggerMessage(this.getIp(), childNode);
                                    NetworkEmulator.sendMessage(trigger);
                                }
                                this.acknowledgements = this.children.length;
                                this.status++;
                            }

                            break;

                        case 1:

                            //Check if task ended message has been received and
                            if (input instanceof TaskEndedMessage) {
                                this.acknowledgements--;
                                if (this.acknowledgements == 0) {
                                    TokenMessage token = new TokenMessage(this.getIp(), this.getNextNeighbour());
                                    NetworkEmulator.sendMessage(token);
                                    this.status = 0;
                                }
                            }

                            break;

                    }

                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private String getNextNeighbour(){
        for(int i = 0 ; i < this.neighbours.length ; i++){
            if(this.neighbours[i].equals(this.getIp())){
                if(i + 1 == this.neighbours.length){
                    return this.neighbours[0];
                }else{
                    return this.neighbours[i+1];
                }
            }
        }
        return null;
    }

}
