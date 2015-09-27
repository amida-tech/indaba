package com.ocs.indaba.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import java.io.File;
import java.util.UUID;

public class ExecuteOsCmdUtil {

    private static final Logger log = Logger.getLogger(ExecuteOsCmdUtil.class);
    private static ExecutorService srvPool = null;

    /**
     * execute OS command
     *
     * @param command
     * @return computed result
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public static int execOsCmd(String command, long maxSecs) throws IOException,
            InterruptedException, ExecutionException, TimeoutException {
        log.debug("=======>>> command:\n" + command);
        final String tmpCmd = command;
        int result = -1;
        Runtime run = Runtime.getRuntime();
        final Process p = run.exec(command);

        log.debug("Job started.");

        if (srvPool == null) {
            srvPool = Executors.newFixedThreadPool(Config.getInt(Constants.KEY_WKHTMLTOPDF_JAVA_EXECUTOR_THREADNUM));
        }

        Future<Integer> future = srvPool.submit(new Callable<Integer>() {

            public Integer call() {
                try {
                    //Need to handle the errorStream, or the process will not end
                    //System.out.println("Tips: "+streamToString(p.getErrorStream()));
                    killOffTheStream(tmpCmd, "<PERROR>", p.getErrorStream());
                    killOffTheStream(tmpCmd, "<POUTPUT>", p.getInputStream());

                    log.debug("Waiting for the job to complete ...");
                    int res = p.waitFor();
                    log.debug("Process completed normally - killed it. Result: " + res);
                    p.destroy();
                    return res;
                } catch (InterruptedException e) {
                    // the subprocess may still be alive
                    log.debug("Interrupted - Killed runnig process");
                    p.destroy();
                }
                return -1;
            }
        });

        try {
            log.debug("Wait for at most " + maxSecs + " seconds");
            result = future.get(maxSecs, TimeUnit.SECONDS);
            log.debug("Job Completed. Result: " + result);
        } catch (Exception e) {
            log.debug("Job timed out!!!");
            result = -1;
        }

        p.destroy();
        log.debug("Killed running process.");

        return result;
    }

    public static void killOffTheStream(final String command, final String prefix, final InputStream in) {
        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        StringBuffer sb = new StringBuffer();
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(isr);
                        String inputLine = null;
                        try {
                            while ((inputLine = reader.readLine()) != null) {
                                if (inputLine.startsWith("[") || inputLine.startsWith("Warning:")) {
                                    continue;
                                }
                                sb.append(inputLine);
                                sb.append("\n");
//								System.out.println(inputLine);
                            }
                            log.debug(prefix + sb + prefix + command);
                        } catch (IOException e) {
                            // e.printStackTrace();
                        }

                        // CLOSEFILE
                        if (isr != null) {
                            try {
                                isr.close();
                            } catch (Exception e) {
                            }
                        }

                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (Exception e) {
                            }
                        }

//						in.close();
//						return sb.toString();
                    }
                }).start();
    }

    /**
     * execute OS command and handle the exception
     *
     * @param command
     * @param errorSb
     * @return computed result
     */
    public static int execOsCmd(String command, StringBuffer errorSb, long maxSecs) {
        String error = "OK";
        int result;

        try {
            result = execOsCmd(command, maxSecs);
        } catch (Exception e) {
            error = e.toString();
            result = -1;
        }

        errorSb.append(error);
        return result;
    }

    public static int execWkhtmltopdf(String basePath, String[] urls, String output, StringBuffer errorSb) {
        String[] tempFiles = new String[urls.length];
        String command;
        int result = -1;
        String finalTempFile;

        for (int i = 0; i < urls.length; i++) {
            tempFiles[i] = basePath + "/" + UUID.randomUUID();

            command = Config.getString(Constants.KEY_WKHTMLTOPDF_CMD) + " " + urls[i] + " " + tempFiles[i];

            result = execOsCmd(command, errorSb, Config.getInt(Constants.KEY_WKHTMLTOPDF_TIMEOUT));

            if (result < 0) {
                return result;
            }
        }

        if (urls.length > 1) {
            // merge files
            finalTempFile = basePath + "/" + UUID.randomUUID();
            command = Config.getString(Constants.KEY_PDFTK_CMD);
            for (String tempFile : tempFiles) {
                command += " " + tempFile;
            }
            command += " cat output " + finalTempFile;

            result = execOsCmd(command, errorSb, Config.getInt(Constants.KEY_PDFTK_TIMEOUT));

            if (result < 0) {
                log.error("Failed to merge PDF files: " + command + " Result: " + result);
                return result;
            }
        } else {
            finalTempFile = tempFiles[0];
        }

        // Now rename the temp file
        File pdfFile = new File(output);

        if (!pdfFile.exists()) {
            if (FileUtil.rename(finalTempFile, output)) {
                log.debug("Renamed file " + finalTempFile + " to " + output);
            } else {
                log.debug("Failed to rename file " + finalTempFile + " to " + output);
                return -1;
            }
        }

        return 0;
    }
}
