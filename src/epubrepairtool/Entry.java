/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool;

/**
 *
 * @author Marco
 */
public class Entry {
    
    private LFH lfHeader;
    private CDFH cdfHeader;
    
    public Entry(LFH lfHeader, CDFH cdfHeader){
        this.lfHeader=lfHeader;
        this.cdfHeader=cdfHeader;
    }
    
    public LFH getLFH(){
        return lfHeader;
    }
    
    public CDFH getCDFH(){
        return cdfHeader;
    }
    
    public boolean mustToBeFixed(){
        byte[] lfRawFilename =lfHeader.getRawFilename();
        byte[] cdfRawFilename=cdfHeader.getRawFilename();
        for(int k=0;k<lfHeader.getFilenameLength();k++){
            if(lfRawFilename[k]!=cdfRawFilename[k]) return true;
        }
        return false;
    }
    
    public void fix(){
        System.arraycopy(lfHeader.getRawFilename(), 0, cdfHeader.getRawFilename(), 0, lfHeader.getRawFilename().length);
    }
    
}
