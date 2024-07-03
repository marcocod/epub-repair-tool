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
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Marco
 */
public class CDFH {
    
    private final static long CDFH_SIGNATURE=0x02014b50;
    private final static long BASE_SIZE=46;
    
    // position
    private long pos;
    
    // content
    private long signature;          //  0- 4
    private long versionMadeBy;      //  4- 6
    private long versionNeeded;      //  6- 8
    private long flags;              //  8-10
    private long compression;        // 10-12
    private long lastModifTime;      // 12-14
    private long lastModifDate;      // 14-16
    private long crc32;              // 16-20
    private long compressedSize;     // 20-24
    private long uncompressedSize;   // 24-28
    private long fileNameLength;     // 28-30
    private long extraFieldLength;   // 30-32
    private long fileCommentLength;  // 32-34
    private long diskStart;          // 34-36
    private long internalAttributes; // 36-38
    private long externalAttributes; // 38-42
    private long relativeOffset;     // 42-46
    private byte[] filename;
    private byte[] extraField;
    private byte[] fileComment;
    
    private CDFH(){
    }
    
    public static CDFH read(RandomAccessFile rafid, long pos) throws IOException{
        CDFH header=new CDFH();
        header.pos=pos;

        ByteBuffer buffer=ByteBuffer.allocate((int)BASE_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] array=buffer.array();

        rafid.seek(pos);
        rafid.read(array);

        header.signature         = Utils.buildUnsigned(buffer.getInt(   0));
        if(header.signature!=CDFH_SIGNATURE) return null;
        
        header.versionMadeBy     = Utils.buildUnsigned(buffer.getShort( 4));
        header.versionNeeded     = Utils.buildUnsigned(buffer.getShort( 6));
        header.flags             = Utils.buildUnsigned(buffer.getShort( 8));
        header.compression       = Utils.buildUnsigned(buffer.getShort(10));
        header.lastModifTime     = Utils.buildUnsigned(buffer.getShort(12));
        header.lastModifDate     = Utils.buildUnsigned(buffer.getShort(14));
        header.crc32             = Utils.buildUnsigned(buffer.getInt(  16));
        header.compressedSize    = Utils.buildUnsigned(buffer.getInt(  20));
        header.uncompressedSize  = Utils.buildUnsigned(buffer.getInt(  24));
        header.fileNameLength    = Utils.buildUnsigned(buffer.getShort(28));
        header.extraFieldLength  = Utils.buildUnsigned(buffer.getShort(30));
        header.fileCommentLength = Utils.buildUnsigned(buffer.getShort(32));
        header.diskStart         = Utils.buildUnsigned(buffer.getShort(34));
        header.internalAttributes= Utils.buildUnsigned(buffer.getShort(36));
        header.externalAttributes= Utils.buildUnsigned(buffer.getInt(  38));
        header.relativeOffset    = Utils.buildUnsigned(buffer.getInt(  42));

        header.filename    = new byte[(int)header.fileNameLength];
        header.extraField  = new byte[(int)header.extraFieldLength];
        header.fileComment = new byte[(int)header.fileCommentLength];

        rafid.read(header.filename);
        rafid.read(header.extraField);
        rafid.read(header.fileComment);

        return header;
    }
    
    public long size(){
        return BASE_SIZE+fileNameLength+extraFieldLength+fileCommentLength;
    }
    
    public long getFilenameLength(){
        return fileNameLength;
    }
    
    public byte[] getRawFilename(){
        return filename;
    }
    
    public String getFilename(){
        return new String(filename, StandardCharsets.UTF_8);
    }
    
    public long getRelativeOffset(){
        return relativeOffset;
    }
    
    public void writeFilenameOnly(RandomAccessFile rafid) throws IOException{
        rafid.seek(pos+BASE_SIZE);
        rafid.write(filename);
    }
    
}
