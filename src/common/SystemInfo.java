package common;

public class SystemInfo {
    
    public String rootDirectory;
    
    public SystemInfo() {}
    
    public SystemInfo( String rootDirectory ) {
        this.rootDirectory = rootDirectory;
    }
    
    public void setRootDirectory( String rootDirectory ) {
        this.rootDirectory = rootDirectory; 
    }
    
    public String getRootDirectory() {
        return rootDirectory;
    }
}
