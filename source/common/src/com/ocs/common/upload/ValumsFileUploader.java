/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.common.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author yc06x
 */

/*
 * This file uploader works with Valums file uploader that does NOT send file as multi-part form data.
 */
public class ValumsFileUploader {
    
    String fileName = null;
    InputStream data = null;
    
    public ValumsFileUploader(HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        fileName = request.getHeader("X-File-Name");
        fileName = URLDecoder.decode(fileName, "UTF-8");
        data = request.getInputStream();
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getData() {
        return data;
    }

}
