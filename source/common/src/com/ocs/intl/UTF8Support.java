/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.intl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 *
 * @author yc06x
 */
public class UTF8Support {

    static public boolean isUTF8Encoded(File file) throws FileNotFoundException, IOException {
        FileInputStream fin = new FileInputStream(file);

      /*
       * Create byte array large enough to hold the content of the file.
       * Use File.length to determine size of the file in bytes.
       */


       byte fileContent[] = new byte[(int)file.length()];

       /*
        * To read content of the file in byte array, use
        * int read(byte[] byteArray) method of java FileInputStream class.
        *
        */
       fin.read(fileContent);

       return isUTF8Encoded(fileContent);
    }

    
    static public boolean isUTF8Encoded(byte[] data) {
        try {
            Charset.availableCharsets().get("UTF-8").newDecoder().decode(ByteBuffer.wrap(data));
        } catch (CharacterCodingException e) {
            return false;
        }

        return true;
    }

}
