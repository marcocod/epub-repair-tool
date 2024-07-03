/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Marco
 */
public class LFH {
    
    private final static long LFH_SIGNATURE=0x04034b50;
    private final static long BASE_SIZE=30;
    
    // position
    private long pos;
    
    // content
    private long signature;        //  0- 4
    private long version;          //  4- 6
    private long flags;            //  6- 8           
    private long compression;      //  8-10
    private long lastModifTime;    // 10-12
    private long lastModifDate;    // 12-14
    private long crc32;            // 14-18
    private long compressedSize;   // 18-22
    private long uncompressedSize; // 22-26
    private long fileNameLength;   // 26-28
    private long extraFieldLength; // 28-30
    private byte[] filename;
    private byte[] extraField;
    
    private LFH(){
    }
    
    public static LFH read(RandomAccessFile rafid, long pos){
        try{
            LFH header=new LFH();
            header.pos=pos;

            ByteBuffer buffer=ByteBuffer.allocate((int)BASE_SIZE);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            byte[] array=buffer.array();

            rafid.seek(pos);
            rafid.read(array);
            
            header.signature        = Utils.buildUnsigned(buffer.getInt(   0));
            if(header.signature!=LFH_SIGNATURE) return null;
            
            header.version          = Utils.buildUnsigned(buffer.getShort( 4));
            header.flags            = Utils.buildUnsigned(buffer.getShort( 6));
            header.compression      = Utils.buildUnsigned(buffer.getShort( 8));
            header.lastModifTime    = Utils.buildUnsigned(buffer.getShort(10));
            header.lastModifDate    = Utils.buildUnsigned(buffer.getShort(12));
            header.crc32            = Utils.buildUnsigned(buffer.getInt(  14));
            header.compressedSize   = Utils.buildUnsigned(buffer.getInt(  18));
            header.uncompressedSize = Utils.buildUnsigned(buffer.getInt(  22));
            header.fileNameLength   = Utils.buildUnsigned(buffer.getShort(26));
            header.extraFieldLength = Utils.buildUnsigned(buffer.getShort(28));
            
            header.filename   = new byte[(int)header.fileNameLength];
            header.extraField = new byte[(int)header.extraFieldLength];
            
            rafid.read(header.filename);
            rafid.read(header.extraField);
            
            return header;
        }catch(IOException ioe){
            return null;
        }
    }
    
    public long size(){
        return BASE_SIZE+this.fileNameLength+this.extraFieldLength;
    }
    
    public long getFilenameLength(){
        return this.fileNameLength;
    }
    
    public byte[] getRawFilename(){
        return this.filename;
    }
    
    public String getFilename(){
        try{
            return new String(this.filename,"UTF-8");
        }catch(UnsupportedEncodingException uee){
            return null;
        }
    }
    
    public void writeFilenameOnly(RandomAccessFile rafid, long pos) throws IOException{
        rafid.seek(pos+(long)BASE_SIZE);
        rafid.write(this.filename);
    }
    
}
