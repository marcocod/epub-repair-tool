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
    
    private final static int LFH_SIGNATURE=0x04034b50;
    private final static int BASE_SIZE=30;
    
    // position
    private long pos;
    
    // content
    private int signature;          //  0- 4
    private short version;          //  4- 6
    private short flags;            //  6- 8           
    private short compression;      //  8-10
    private short lastModifTime;    // 10-12
    private short lastModifDate;    // 12-14
    private int crc32;              // 14-18
    private int compressedSize;     // 18-22
    private int uncompressedSize;   // 22-26
    private short fileNameLength;   // 26-28
    private short extraFieldLength; // 28-30
    private byte[] filename;
    private byte[] extraField;
    
    private LFH(){
    }
    
    public static LFH read(RandomAccessFile rafid, long pos){
        try{
            LFH header=new LFH();
            header.pos=pos;

            ByteBuffer buffer=ByteBuffer.allocate(BASE_SIZE);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            byte[] array=buffer.array();

            rafid.seek(pos);
            rafid.read(array);
            
            header.signature        = buffer.getInt(   0);
            if(header.signature!=LFH_SIGNATURE) return null;
            header.version          = buffer.getShort( 4);
            header.flags            = buffer.getShort( 6);
            header.compression      = buffer.getShort( 8);
            header.lastModifTime    = buffer.getShort(10);
            header.lastModifDate    = buffer.getShort(12);
            header.crc32            = buffer.getInt(  14);
            header.compressedSize   = buffer.getInt(  18);
            header.uncompressedSize = buffer.getInt(  22);
            header.fileNameLength   = buffer.getShort(26);
            header.extraFieldLength = buffer.getShort(28);
            
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
        return (long)BASE_SIZE+(long)this.fileNameLength+(long)this.extraFieldLength;
    }
    
    public int getFilenameLength(){
        return (int)this.fileNameLength;
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
