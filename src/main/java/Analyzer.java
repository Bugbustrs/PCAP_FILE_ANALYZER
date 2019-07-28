import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.packet.Packet;
import io.pkts.packet.UDPPacket;
import io.pkts.protocol.Protocol;
import org.apache.commons.vfs2.FileObject;

import java.io.File;
import java.io.IOException;

public class Analyzer  implements Runnable{

    private FileObject[] pcapFiles;
    private PacketHandler handler;

    public Analyzer(FileObject[] pf){
        pcapFiles = pf;
        handler = new PacketHandlerImp();
    }

    @Override
    public void run() {
        for(FileObject f: pcapFiles){
            try {
                if(!f.getName().getFriendlyURI().endsWith(".small")) {
                    processPcapFile(Pcap.openStream(f.getContent().getInputStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(((PacketHandlerImp)handler).getTopTenDest());
    }

    private void processPcapFile(Pcap pcap){
        try {
            pcap.loop(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
