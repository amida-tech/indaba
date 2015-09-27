/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.common.upload;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MutiFileUpload extends FileUploadBase {

    private Map<String, FileItem> files;// 
    private long filesSize = 0; // 

    public void parseRequest(HttpServletRequest request)
            throws Exception {

        files = new HashMap<String, FileItem>();

        // Create a factory for disk-based file items

        FileItemFactory factory = new DiskFileItemFactory();

        //factory.setSizeThreshold(sizeThreshold);

        if (repository != null) {
            //factory.setRepository(repository);
        }

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding(encoding);

        List<FileItem> items = upload.parseRequest(request);

        for (FileItem item : items) {
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                String value = item.getString(encoding);
                parameters.put(fieldName, value);
            } else {

                if (super.isValidFile(item)) {
                    continue;
                }

                String fieldName = item.getFieldName();

                files.put(fieldName, item);
                filesSize += item.getSize();
            }
        }
    }

    /**
     * parseRequest(HttpServletRequest request)
     *
     * @param parent
     * @throws Exception
     */
    public void upload(File parent) throws Exception {
        if (files.isEmpty()) {
            return;
        }

        if (sizeMax > -1 && filesSize > super.sizeMax) {
            String message = String.format("the request was rejected because its size (%1$s) exceeds the configured maximum (%2$s)", filesSize, super.sizeMax);

            throw new SizeLimitExceededException(message, filesSize, super.sizeMax);
        }

        for (String key : files.keySet()) {
            FileItem item = files.get(key);
            String name = item.getName();

            File file = new File(parent, name);
            item.write(file);
        }
    }

    public Map<String, FileItem> getFiles() {
        return files;
    }
}
