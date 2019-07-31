import io.pkts.PacketHandler;
import io.pkts.Pcap;
import org.apache.commons.vfs2.FileObject;

import java.io.IOException;
import java.util.Set;

public class Analyzer implements Runnable {
    private PacketHandler handler;
    private FileMonitor monitor;

    public Analyzer(FileMonitor monitor) {
        handler = new PacketHandlerImp();
        this.monitor = monitor;
    }

    public Analyzer(){
        this(new FileMonitor(Config.HOST_NAME));
    }

    @Override
    public void run() {
        Set<FileObject> pcapFiles = monitor.getCreatedFiles();
        for (FileObject f : pcapFiles) {
            try {
                processPcapFile(Pcap.openStream(f.getContent().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        monitor.doneProcessing();
        //will send here to the orchastrator
        ((PacketHandlerImp)handler).clearList();
    }

    private void processPcapFile(Pcap pcap) {
        try {
            pcap.loop(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
