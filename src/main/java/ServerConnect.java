import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

import java.io.IOException;

public class ServerConnect {

    public static void main(String[] args) throws IOException {
        FileSystemManager manager = VFS.getManager();
        FileObject file = manager.resolveFile("\\\\asustor2.cs.uct.ac.za\\inethi_logs");
        FileObject[] children = file.getChildren();
        Analyzer analyzer = new Analyzer(children);
        Thread th = new Thread(analyzer);
        th.run();
}
}
