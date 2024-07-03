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
    
    private boolean analyzed=false;
    private boolean fixed=false;
    private EOCD eocd=null;
    private ArrayList<Entry> entryList=null;
    
    public Epub(File file){
        this.file=file;
    }
    
    public Epub(String filename){
        this.file=new File(filename);
    }
    
    public File getFile(){
        return this.file;
    }
    
    public void analyze() throws IOException{
        RandomAccessFile rafid=new RandomAccessFile(this.file, "r");

        long eocdPos=EOCD.locateEOCD(rafid);
        if(eocdPos>=0L){
            EOCD eocd=EOCD.read(rafid, eocdPos);
            int totalEntries=eocd.getTotalEntries();
            entryList=new ArrayList<Entry>();
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
                    entryList.add(entry);
                }
            }
        }
        rafid.close();
        analyzed=true;
    }
    
    public boolean mustToBeFixed(){
        if(entryList!=null){
            for(int k=0;k<entryList.size();k++){
                if(entryList.get(k).mustToBeFixed()){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }
    
    public void fix() throws IOException{
        if(this.analyzed==true && this.fixed==false){
            RandomAccessFile rafid=new RandomAccessFile(this.file, "rw");
            for(int k=0;k<entryList.size();k++){
                Entry entry=entryList.get(k);
                if(entry.mustToBeFixed()==true){
                    entry.fix();
                    entry.getCDFH().writeFilenameOnly(rafid);
                }
            }
            rafid.close();
            fixed=true;
        }
    }
    
    public String[][] getInvalidFilenames(){
        if(entryList!=null){
            String[][] invalidFilenames=new String[entryList.size()][2];
            for(int k=0;k<entryList.size();k++){
                Entry entry=entryList.get(k);
                invalidFilenames[k][0]=entry.getLFH().getFilename();
                invalidFilenames[k][1]=entry.getCDFH().getFilename();
            }
            return invalidFilenames;
        }else{
            return null;
        }
    }
    
}
