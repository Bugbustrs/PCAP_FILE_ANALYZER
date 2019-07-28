import io.pkts.PacketHandler;
import io.pkts.Pcap;
import org.apache.commons.vfs2.FileObject;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Analyzer implements Runnable {
    PacketHandler handler;

    public Analyzer() {
        handler = new PacketHandlerImp();
    }

    @Override
    public void run() {
        Set<FileObject> pcapFiles = ServerConnect.getFileMonitor().getCreatedFiles();
        for (FileObject f : pcapFiles) {
            try {
                processPcapFile(Pcap.openStream(f.getContent().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(((PacketHandlerImp) handler).getTopTenDest());
    }

    private void processPcapFile(Pcap pcap) {
        try {
            pcap.loop(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
