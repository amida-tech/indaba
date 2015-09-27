/*
 * To change this template); map.put( choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.common;

import com.ocs.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jeff
 */
public class ContentType {

    private static final Map<String, String> map = new HashMap<String, String>();
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    static {
        map.put("ez", "application/andrew-inset");
        map.put("hqx", "application/mac-binhex40");
        map.put("cpt", "application/mac-compactpro");
        map.put("doc", "application/msword");
        map.put("bin", "application/octet-stream");
        map.put("dms", "application/octet-stream");
        map.put("lha", "application/octet-stream");
        map.put("lzh", "application/octet-stream");
        map.put("exe", "application/octet-stream");
        map.put("class", "application/octet-stream");
        map.put("so", "application/octet-stream");
        map.put("dll", "application/octet-stream");
        map.put("oda", "application/oda");
        map.put("pdf", "application/pdf");
        map.put("ai", "application/postscript");
        map.put("eps", "application/postscript");
        map.put("ps", "application/postscript");
        map.put("smi", "application/smil");
        map.put("smil", "application/smil");
        map.put("mif", "application/vnd.mif");
        map.put("xls", "application/vnd.ms-excel");
        map.put("ppt", "application/vnd.ms-powerpoint");
        map.put("wbxml", "application/vnd.wap.wbxml");
        map.put("wmlc", "application/vnd.wap.wmlc");
        map.put("wmlsc", "application/vnd.wap.wmlscriptc");
        map.put("bcpio", "application/x-bcpio");
        map.put("vcd", "application/x-cdlink");
        map.put("pgn", "application/x-chess-pgn");
        map.put("cpio", "application/x-cpio");
        map.put("csh", "application/x-csh");
        map.put("dcr", "application/x-director");
        map.put("dir", "application/x-director");
        map.put("dxr", "application/x-director");
        map.put("dvi", "application/x-dvi");
        map.put("spl", "application/x-futuresplash");
        map.put("gtar", "application/x-gtar");
        map.put("hdf", "application/x-hdf");
        map.put("js", "application/x-javascript");
        map.put("skp", "application/x-koan");
        map.put("skd", "application/x-koan");
        map.put("skt", "application/x-koan");
        map.put("skm", "application/x-koan");
        map.put("latex", "application/x-latex");
        map.put("nc", "application/x-netcdf");
        map.put("cdf", "application/x-netcdf");
        map.put("sh", "application/x-sh");
        map.put("shar", "application/x-shar");
        map.put("swf", "application/x-shockwave-flash");
        map.put("sit", "application/x-stuffit");
        map.put("sv4cpio", "application/x-sv4cpio");
        map.put("sv4crc", "application/x-sv4crc");
        map.put("tar", "application/x-tar");
        map.put("tcl", "application/x-tcl");
        map.put("tex", "application/x-tex");
        map.put("texinfo", "application/x-texinfo");
        map.put("texi", "application/x-texinfo");
        map.put("t", "application/x-troff");
        map.put("tr", "application/x-troff");
        map.put("roff", "application/x-troff");
        map.put("man", "application/x-troff-man");
        map.put("me", "application/x-troff-me");
        map.put("ms", "application/x-troff-ms");
        map.put("ustar", "application/x-ustar");
        map.put("src", "application/x-wais-source");
        map.put("xhtml", "application/xhtml+xml");
        map.put("xht", "application/xhtml+xml");
        map.put("zip", "application/zip");
        map.put("au", "audio/basic");
        map.put("snd", "audio/basic");
        map.put("mid", "audio/midi");
        map.put("midi", "audio/midi");
        map.put("kar", "audio/midi");
        map.put("mpga", "audio/mpeg");
        map.put("mp2", "audio/mpeg");
        map.put("mp3", "audio/mpeg");
        map.put("aif", "audio/x-aiff");
        map.put("aiff", "audio/x-aiff");
        map.put("aifc", "audio/x-aiff");
        map.put("m3u", "audio/x-mpegurl");
        map.put("ram", "audio/x-pn-realaudio");
        map.put("rm", "audio/x-pn-realaudio");
        map.put("rpm", "audio/x-pn-realaudio-plugin");
        map.put("ra", "audio/x-realaudio");
        map.put("wav", "audio/x-wav");
        map.put("pdb", "chemical/x-pdb");
        map.put("xyz", "chemical/x-xyz");
        map.put("bmp", "image/bmp");
        map.put("gif", "image/gif");
        map.put("ief", "image/ief");
        map.put("jpeg", "image/jpeg");
        map.put("jpg", "image/jpeg");
        map.put("jpe", "image/jpeg");
        map.put("png", "image/png");
        map.put("tiff", "image/tiff");
        map.put("tif", "image/tiff");
        map.put("djvu", "image/vnd.djvu");
        map.put("djv", "image/vnd.djvu");
        map.put("wbmp", "image/vnd.wap.wbmp");
        map.put("ras", "image/x-cmu-raster");
        map.put("pnm", "image/x-portable-anymap");
        map.put("pbm", "image/x-portable-bitmap");
        map.put("pgm", "image/x-portable-graymap");
        map.put("ppm", "image/x-portable-pixmap");
        map.put("rgb", "image/x-rgb");
        map.put("xbm", "image/x-xbitmap");
        map.put("xpm", "image/x-xpixmap");
        map.put("xwd", "image/x-xwindowdump");
        map.put("igs", "model/iges");
        map.put("iges", "model/iges");
        map.put("msh", "model/mesh");
        map.put("mesh", "model/mesh");
        map.put("silo", "model/mesh");
        map.put("wrl", "model/vrml");
        map.put("vrml", "model/vrml");
        map.put("css", "text/css");
        map.put("html", "text/html");
        map.put("htm", "text/html");
        map.put("asc", "text/plain");
        map.put("txt", "text/plain");
        map.put("rtx", "text/richtext");
        map.put("rtf", "text/rtf");
        map.put("sgml", "text/sgml");
        map.put("sgm", "text/sgml");
        map.put("tsv", "text/tab-separated-values");
        map.put("wml", "text/vnd.wap.wml");
        map.put("wmls", "text/vnd.wap.wmlscript");
        map.put("etx", "text/x-setext");
        map.put("xsl", "text/xml");
        map.put("xml", "text/xml");
        map.put("mpeg", "video/mpeg");
        map.put("mpg", "video/mpeg");
        map.put("mpe", "video/mpeg");
        map.put("qt", "video/quicktime");
        map.put("mov", "video/quicktime");
        map.put("mxu", "video/vnd.mpegurl");
        map.put("avi", "video/x-msvideo");
        map.put("movie", "video/x-sgi-movie");
        map.put("ice", "x-conference/x-cooltalk");
    }

    public static String getContentType(String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            return DEFAULT_CONTENT_TYPE;
        }

        String contentType = map.get(suffix);
        return (contentType == null) ? DEFAULT_CONTENT_TYPE : contentType;
    }
}
