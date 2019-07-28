import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FileMonitor {

    private String hostName;
    private FileObject file;
    private Set<FileObject> createdFileObject;
    private DefaultFileMonitor defaultFileMonitor;

    public FileMonitor(String hostName) {
        this.hostName = hostName;
        this.defaultFileMonitor = new DefaultFileMonitor(new RemoteFileListener());
        this.createdFileObject = new HashSet<>();
    }


    private class RemoteFileListener implements FileListener {

        @Override
        public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {
            //first check if the file is not
            getAllFiles(fileChangeEvent.getFile());
        }

        @Override
        public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception {
            createdFileObject.remove(fileChangeEvent.getFile());
        }

        @Override
        public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception {
            //we don't expcet pcap files to change
            FileObject temp = fileChangeEvent.getFile();
            if(createdFileObject.contains(temp)){
                createdFileObject.remove(temp);
                createdFileObject.add(temp);

            }
        }

        /***
         * This just checks if the filename ends with pcap or pcapdump just to ensure that it is a pcap file. A more rigorous checkcan be implemented.
         * @param fileName
         * @return true if it is a pcap file and false if not
         */
        private boolean isValidFile(String fileName){
            return fileName.endsWith("pcap");
        }

        /**
         * This method will get all pcap file and pcapdump files from the file. If the file is folder we recurse in all the folders within and get all the files
         * Might want to change this method so it is iterative and not recursive.
         * @param fileObject
         * @throws FileSystemException
         */
        private void getAllFiles(FileObject fileObject) throws FileSystemException {
            if(fileObject.isFile() && isValidFile(fileObject.getPublicURIString())) {
                createdFileObject.add(fileObject);
            }else if (fileObject.isFolder()){
                for(FileObject file: fileObject.getChildren()){
                    getAllFiles(file);
                }
            }
        }

        private void start(){
            //adding a non daemon thread sine default monitor is run as a daemon thread
            Thread tp = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true);
                }
            });
            tp.start();
            try {
                file = VFS.getManager().resolveFile(hostName);
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
            defaultFileMonitor.addFile(file);
            defaultFileMonitor.setDelay(5000);
            defaultFileMonitor.start();
        }
    }

    public Set<FileObject> getCreatedFiles(){
        return createdFileObject;
    }

}
