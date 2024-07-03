/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Marco
 */
public class EOCD {
    
    private final static long EOCD_SIGNATURE=0x06054b50;
    private final static long BASE_SIZE=22;
    
    private final static int EOCD_MAX_SIZE=65558;
    
    // position
    private long pos;
    
    // content
    private long signature;              //  0- 4
    private long diskNumber;             //  4- 6
    private long diskStarts;             //  6- 8
    private long diskEntries;            //  8-10
    private long totalEntries;           // 10-12
    private long centralDirectorySize;   // 12-16
    private long centralDirectoryOffset; // 16-20
    private long commentLength;          // 20-22
    private byte[] comment;
    
    private EOCD(){
    }
    
    public static EOCD read(RandomAccessFile rafid, long pos) throws IOException{
        EOCD eocd=new EOCD();
        eocd.pos=pos;

        ByteBuffer buffer=ByteBuffer.allocate((int)BASE_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] array=buffer.array();

        rafid.seek(pos);
        rafid.read(array);

        eocd.signature              = Utils.buildUnsigned(buffer.getInt(   0));
        if(eocd.signature!=EOCD_SIGNATURE) return null;
        
        eocd.diskNumber             = Utils.buildUnsigned(buffer.getShort( 4));
        eocd.diskStarts             = Utils.buildUnsigned(buffer.getShort( 6));
        eocd.diskEntries            = Utils.buildUnsigned(buffer.getShort( 8));
        eocd.totalEntries           = Utils.buildUnsigned(buffer.getShort(10));
        eocd.centralDirectorySize   = Utils.buildUnsigned(buffer.getInt(  12));
        eocd.centralDirectoryOffset = Utils.buildUnsigned(buffer.getInt(  16));
        if(eocd.centralDirectoryOffset==0xffffffff) return null; // ZIP64
        
        eocd.commentLength          = Utils.buildUnsigned(buffer.getShort(20));
        
        eocd.comment = new byte[(int)eocd.commentLength];

        rafid.read(eocd.comment);

        return eocd;
    }
    
    public long size(){
        return BASE_SIZE+commentLength;
    }
    
    public static long locateEOCD(RandomAccessFile rafid) throws IOException{
        long endPos=rafid.length();
        int bufferSize=EOCD_MAX_SIZE;
        if(endPos<(long)bufferSize){
            bufferSize=(int)endPos;
        }
        ByteBuffer buffer=ByteBuffer.allocate(bufferSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] array=buffer.array();
        rafid.seek(endPos-bufferSize);
        rafid.read(array);

        for(int k=bufferSize-4;k>=0;k--){
            long signature=Utils.buildUnsigned(buffer.getInt(k));
            if(signature==EOCD_SIGNATURE){
                return endPos-bufferSize+k;
            }
        }

        return -1L;
    }
    
    public int getTotalEntries(){
        return (int)totalEntries;
    }
    
    public long getCentralDirectoryOffset(){
        return centralDirectoryOffset;
    }
    
}
