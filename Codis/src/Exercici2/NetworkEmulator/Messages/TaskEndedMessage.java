package Exercici2.NetworkEmulator.Messages;

public class TaskEndedMessage extends BaseMessage{

    public TaskEndedMessage(String sourceNode, String destinationNode) {
        super(BaseMessage.TASK_ENDED_MESSAGE, sourceNode, destinationNode);
    }

}
