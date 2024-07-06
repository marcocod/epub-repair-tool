/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author Marco
 */
public class Epub {
    
    private File file;
    
    private EOCD eocd=null;
    private ArrayList<Entry> invalidEntryList=null;
    
    public Epub(File file){
        this.file=file;
    }
    
    public Epub(String filename){
        file=new File(filename);
    }
    
    public File getFile(){
        return file;
    }
    
    public void analyze() throws IOException{
        RandomAccessFile rafid=new RandomAccessFile(file, "r");

        long eocdPos=EOCD.locateEOCD(rafid);
        if(eocdPos>=0L){
            eocd=EOCD.read(rafid, eocdPos);
            if(eocd!=null){
                int totalEntries=eocd.getTotalEntries();
                invalidEntryList=new ArrayList<Entry>();
                long pos=eocd.getCentralDirectoryOffset();
                for(int k=0;k<totalEntries;k++){
                    CDFH cdfHeader=CDFH.read(rafid, pos);
                    pos+=cdfHeader.size();
                    LFH lfHeader=LFH.read(rafid,cdfHeader.getRelativeOffset());
                    if(cdfHeader.getFilenameLength()!=lfHeader.getFilenameLength()){
                        throw new IOException("Filename length doesn't match in headers");
                    }
                    Entry entry=new Entry(lfHeader,cdfHeader);
                    if(entry.mustToBeFixed()){
                        invalidEntryList.add(entry);
                    }
                }
            }
        }
        rafid.close();
    }
    
    public boolean mustToBeFixed(){
        if(eocd!=null && invalidEntryList!=null){
            return invalidEntryList.size()>0;
        }else{
            return false;
        }
    }
    
    public void fix() throws IOException{
        if(this.mustToBeFixed()==true){
            if(file.canWrite()){
                RandomAccessFile rafid=new RandomAccessFile(file, "rw");
                for(int k=0;k<invalidEntryList.size();k++){
                    Entry entry=invalidEntryList.get(k);
                    if(entry.mustToBeFixed()==true){
                        entry.fix();
                        entry.getCDFH().writeFilenameOnly(rafid);
                    }
                }
                rafid.close();
            }
        }
    }
    
    public String[][] getInvalidFilenames(){
        if(invalidEntryList!=null){
            String[][] invalidFilenames=new String[invalidEntryList.size()][2];
            for(int k=0;k<invalidEntryList.size();k++){
                Entry entry=invalidEntryList.get(k);
                if(entry.mustToBeFixed()==true){
                    invalidFilenames[k][0]=entry.getLFH().getFilename();
                    invalidFilenames[k][1]=entry.getCDFH().getFilename();
                }
            }
            return invalidFilenames;
        }else{
            return null;
        }
    }
    
}
