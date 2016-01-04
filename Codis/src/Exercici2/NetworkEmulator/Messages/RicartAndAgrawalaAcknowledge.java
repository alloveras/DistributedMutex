package Exercici2.NetworkEmulator.Messages;

public class RicartAndAgrawalaAcknowledge extends TimestampedBaseMessage{

    public RicartAndAgrawalaAcknowledge(String sourceNode, String destinationNode, int timestamp) {
        super(BaseMessage.RA_ACKNOWLEDGE, sourceNode, destinationNode, timestamp);
    }

}
