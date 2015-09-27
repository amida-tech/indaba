/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SourceFile;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class SourceFileDAO extends SmartDaoMySqlImpl<SourceFile, Integer> {

    private static final Logger log = Logger.getLogger(SourceFileDAO.class);
    private static final String SELECT_SOURCE_FILE_BY_FILENAME = "SELECT * FROM source_file WHERE filename=?";
    private static final String SELECT_SOURCE_FILES_BY_ACTIVE = "SELECT * FROM source_file WHERE status=1 ORDER BY id";
    private static final String SELECT_SOURCE_FILES_BY_FILENAME_LIKE = "SELECT * FROM source_file WHERE filename LIKE ''%{0}%'' AND status=1 ORDER BY id";

    public SourceFile selectSourceFileByFilename(String filename) {
        return super.findSingle(SELECT_SOURCE_FILE_BY_FILENAME, filename);
    }

    public List<SourceFile> selectActiveSourceFiles() {
        return super.find(SELECT_SOURCE_FILES_BY_ACTIVE);
    }

    public List<SourceFile> selectSourceFilesByFilenameLike(String searchFilename) {
        return super.find(MessageFormat.format(SELECT_SOURCE_FILES_BY_FILENAME_LIKE, searchFilename));
    }
}
