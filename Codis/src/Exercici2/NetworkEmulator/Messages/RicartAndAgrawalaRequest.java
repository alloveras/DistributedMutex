package Exercici2.NetworkEmulator.Messages;

public class RicartAndAgrawalaRequest extends TimestampedBaseMessage {

    public RicartAndAgrawalaRequest(String sourceNode, String destinationNode, int timestamp) {
        super(BaseMessage.RA_REQUEST, sourceNode, destinationNode, timestamp);
    }

}
