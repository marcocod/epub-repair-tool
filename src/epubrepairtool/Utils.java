/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Marco
 */
public class Utils {
    
    private static final ByteBuffer BUFFER;
    
    static{
        BUFFER=ByteBuffer.allocate(8);
        BUFFER.order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public static long buildUnsigned(int value){
        BUFFER.putLong(0L); // reset
        BUFFER.putInt(0, value);
        return BUFFER.getLong();
    }
    
    public static long buildUnsigned(short value){
        BUFFER.putLong(0L); // reset
        BUFFER.putShort(0, value);
        return BUFFER.getLong();
    }
    
}