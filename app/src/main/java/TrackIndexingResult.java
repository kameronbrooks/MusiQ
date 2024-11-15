import android.media.MediaPlayer;

import java.util.LinkedList;

public class TrackIndexingResult {
    private LinkedList<TrackInfo> _newIndexedItems;
    private int _newItemCount;

    TrackIndexingResult() {
        _newIndexedItems = new LinkedList<TrackInfo>();
        _newItemCount = 0;
    }

    public void AddItem(TrackInfo track) {
        _newIndexedItems.add(track);
        _newItemCount++;
    }
    public static TrackIndexingResult concat(TrackIndexingResult a, TrackIndexingResult b) {
        TrackIndexingResult output = new TrackIndexingResult();
        // Add all elements from both
        output._newIndexedItems.addAll(a._newIndexedItems);
        output._newIndexedItems.addAll(b._newIndexedItems);
        // Update the count
        output._newItemCount = a._newItemCount + b._newItemCount;
        return output;
    }

    public TrackInfo[] getTracks() {
        TrackInfo[] newArray = new TrackInfo[_newIndexedItems.size()];
        return _newIndexedItems.toArray(newArray);
    }
}
