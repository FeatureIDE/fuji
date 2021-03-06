package lancs.mobilemedia.core.ui.datamodel;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import de.ovgu.cide.jakutil.*;
public class VideoAlbumData extends AlbumData {
  public VideoAlbumData(){
    mediaAccessor=new VideoMediaAccessor(this);
  }
  public InputStream getVideoFromRecordStore(  String recordStore,  String musicName) throws ImageNotFoundException, PersistenceMechanismException {
    MediaData mediaInfo=null;
    mediaInfo=mediaAccessor.getMediaInfo(musicName);
    int mediaId=mediaInfo.getForeignRecordId();
    String album=mediaInfo.getParentAlbumName();
    byte[] musicData=(mediaAccessor).loadMediaBytesFromRMS(album,mediaId);
    return new ByteArrayInputStream(musicData);
  }
}
