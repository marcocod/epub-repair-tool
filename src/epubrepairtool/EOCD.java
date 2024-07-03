/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Marco
 */
public class EOCD {
    
    private final static int EOCD_SIGNATURE=0x06054b50;
    private final static int BASE_SIZE=22;
    
    private final static int BUFFER_SIZE=4096;
    private final static int EOCD_MAX_SIZE=65558;
    
    // position
    private long pos;
    
    // content
    private int signature;              //  0- 4
    private short diskNumber;           //  4- 6
    private short diskStarts;           //  6- 8
    private short diskEntries;          //  8-10
    private short totalEntries;         // 10-12
    private int centralDirectorySize;   // 12-16
    private int centralDirectoryOffset; // 16-20
    private short commentLength;        // 20-22
    private byte[] comment;
    
    private EOCD(){
    }
    
    public static EOCD read(RandomAccessFile rafid, long pos) throws IOException{
        EOCD eocd=new EOCD();
        eocd.pos=pos;

        ByteBuffer buffer=ByteBuffer.allocate(BASE_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] array=buffer.array();

        rafid.seek(pos);
        rafid.read(array);

        eocd.signature              = buffer.getInt(   0);
        if(eocd.signature!=EOCD_SIGNATURE) return null;
        eocd.diskNumber             = buffer.getShort( 4);
        eocd.diskStarts             = buffer.getShort( 6);
        eocd.diskEntries            = buffer.getShort( 8);
        eocd.totalEntries           = buffer.getShort(10);
        eocd.centralDirectorySize   = buffer.getInt(  12);
        eocd.centralDirectoryOffset = buffer.getInt(  16);
        eocd.commentLength          = buffer.getShort(20);

        eocd.comment = new byte[(int)eocd.commentLength];

        rafid.read(eocd.comment);

        return eocd;
    }
    
    public long size(){
        return (long)BASE_SIZE+(long)this.commentLength;
    }
    
    public static long localeEOCD(RandomAccessFile rafid) throws IOException{
        long size=rafid.length();
        long endPos=size;
        while(true){
            long value=localeEOCD(rafid,endPos);
            if(value>=0L){
                return value;
            }
            endPos-=BUFFER_SIZE-3;
            if(endPos<=0L){
                return -1L;
            }
            if(size-endPos>EOCD_MAX_SIZE){
                return -1L;
            }
        }
    }
    
    private static long localeEOCD(RandomAccessFile rafid, long endPos) throws IOException{
        int bufferSize=BUFFER_SIZE;
        if(endPos<(long)bufferSize){
            bufferSize=(int)endPos;
        }
        ByteBuffer buffer=ByteBuffer.allocate(bufferSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] array=buffer.array();
        rafid.seek(endPos-bufferSize);
        rafid.read(array);

        for(int k=bufferSize-4;k>=0;k--){
            int signature=buffer.getInt(k);
            if(signature==EOCD_SIGNATURE){
                return endPos-bufferSize+k;
            }
        }

        return -1L;
    }
    
    public int getTotalEntries(){
        return (int)this.totalEntries;
    }
    
    public long getCentralDirectoryOffset(){
        return (long)this.centralDirectoryOffset;
    }
    
}
