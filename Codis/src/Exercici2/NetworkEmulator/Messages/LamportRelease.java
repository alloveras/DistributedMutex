package Exercici2.NetworkEmulator.Messages;

public class LamportRelease extends TimestampedBaseMessage {

    public LamportRelease(String sourceNode, String destinationNode, int timestamp) {
        super(BaseMessage.LAMPORT_RELEASE, sourceNode, destinationNode, timestamp);
    }
}
