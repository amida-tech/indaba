/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.common.upload;

import com.ocs.util.StringUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

/**
 *
 * @author Jeff
 */
public class FileUploadBase {
    protected Map<String, String> parameters = new HashMap<String, String>();// 
    
    protected String encoding = "utf-8"; //

    protected UploadFileFilter filter = null; // 
    
    /**
     * The directory in which uploaded files will be stored, if stored on disk.
     */
    protected int sizeThreshold = DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD;

    /**
     * 
     * The maximum size permitted for the complete request, as opposed to
     * 
     * {@link #fileSizeMax}. A value of -1 indicates no maximum.
     * 
     */
    protected long sizeMax = -1;

    /**
     * The directory in which uploaded files will be stored, if stored on disk.
     */
    protected File repository;
    
    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 
     * @return
     */
    public long getSizeMax() {
        return sizeMax;
    }

    /**
     *
     * @param sizeMax
     */
    public void setSizeMax(long sizeMax) {
        this.sizeMax = sizeMax;
    }

    public int getSizeThreshold() {
        return sizeThreshold;
    }

    public void setSizeThreshold(int sizeThreshold) {
        this.sizeThreshold = sizeThreshold;
    }

    /**
     * Returns the directory used to temporarily store files that are larger
     * than the configured size threshold.
     * 
     * @return The directory in which temporary files will be located.
     * 
     * @see #setRepository(java.io.File)
     * 
     */
    public File getRepository() {
        return repository;
    }

    /**
     * Sets the directory used to temporarily store files that are larger than
     * the configured size threshold.
     * 
     * @param repository
     *            The directory in which temporary files will be located.
     * 
     * @see #getRepository()
     * 
     */
    public void setRepository(File repository) {
        this.repository = repository;
    }
    
    /**
     * 
     * @return
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * 
     * @return
     */
    public UploadFileFilter getFilter() {
        return filter;
    }

    /**
     * 
     * @param filter
     */
    public void setFilter(UploadFileFilter filter) {
        this.filter = filter;
    }
    
    /**
     * 
     * @param item
     * @return
     */
    protected boolean isValidFile(FileItem item){
        return item == null || StringUtils.isEmpty(item.getName()) || item.getSize() == 0 || (filter != null && !filter.accept(item.getName())) ? false : true;
    }
}
