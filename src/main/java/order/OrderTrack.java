package order;

import lombok.Data;

@Data
public class OrderTrack {

    private int track;

    public OrderTrack(int track) {
        this.track = track;
    }
}
