import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

import java.io.IOException;

public class ServerConnect {
    private static FileMonitor fileMonitor;

    public static FileMonitor getFileMonitor(){
        return fileMonitor;
    }

    public static void main(String[] args) throws IOException {
        fileMonitor  = new FileMonitor("\\\\asustor2.cs.uct.ac.za\\inethi_logs");
}
}
