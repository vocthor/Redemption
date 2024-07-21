package redemption.server.event;

public abstract class NetworkEvent extends Event {

    public NetworkEvent() {
        super();
    }

    @Override
    public String toString() {
        return "Network Event";
    }

    public abstract void processEvent();
}
