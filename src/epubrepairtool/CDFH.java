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
public class CDFH {
    
    private final static int CDFH_SIGNATURE=0x02014b50;
    private final static int BASE_SIZE=46;
    
    // position
    private long pos;
    
    // content
    private int signature;            //  0- 4
    private short versionMadeBy;      //  4- 6
    private short versionNeeded;      //  6- 8
    private short flags;              //  8-10
    private short compression;        // 10-12
    private short lastModifTime;      // 12-14
    private short lastModifDate;      // 14-16
    private int crc32;                // 16-20
    private int compressedSize;       // 20-24
    private int uncompressedSize;     // 24-28
    private short fileNameLength;     // 28-30
    private short extraFieldLength;   // 30-32
    private short fileCommentLength;  // 32-34
    private short diskStart;          // 34-36
    private short internalAttributes; // 36-38
    private int externalAttributes;   // 38-42
    private int relativeOffset;       // 42-46
    private byte[] filename;
    private byte[] extraField;
    private byte[] fileComment;
    
    private CDFH(){
    }
    
    public static CDFH read(RandomAccessFile rafid, long pos) throws IOException{
        CDFH header=new CDFH();
        header.pos=pos;

        ByteBuffer buffer=ByteBuffer.allocate(BASE_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] array=buffer.array();

        rafid.seek(pos);
        rafid.read(array);

        header.signature         = buffer.getInt(   0);
        if(header.signature!=CDFH_SIGNATURE) return null;
        header.versionMadeBy     = buffer.getShort( 4);
        header.versionNeeded     = buffer.getShort( 6);
        header.flags             = buffer.getShort( 8);
        header.compression       = buffer.getShort(10);
        header.lastModifTime     = buffer.getShort(12);
        header.lastModifDate     = buffer.getShort(14);
        header.crc32             = buffer.getInt(  16);
        header.compressedSize    = buffer.getInt(  20);
        header.uncompressedSize  = buffer.getInt(  24);
        header.fileNameLength    = buffer.getShort(28);
        header.extraFieldLength  = buffer.getShort(30);
        header.fileCommentLength = buffer.getShort(32);
        header.diskStart         = buffer.getShort(34);
        header.internalAttributes= buffer.getShort(36);
        header.externalAttributes= buffer.getInt(  38);
        header.relativeOffset    = buffer.getInt(  42);

        header.filename    = new byte[(int)header.fileNameLength];
        header.extraField  = new byte[(int)header.extraFieldLength];
        header.fileComment = new byte[(int)header.fileCommentLength];

        rafid.read(header.filename);
        rafid.read(header.extraField);
        rafid.read(header.fileComment);

        return header;
    }
    
    public long size(){
        return (long)BASE_SIZE+(long)fileNameLength+(long)extraFieldLength+(long)fileCommentLength;
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
    
    public long getRelativeOffset(){
        return (long)this.relativeOffset;
    }
    
    public void writeFilenameOnly(RandomAccessFile rafid) throws IOException{
        rafid.seek(this.pos+(long)BASE_SIZE);
        rafid.write(this.filename);
    }
    
}
