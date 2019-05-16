package com.udn.ntpc.od.common.util;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static void zip(File inputFile, File outputZipFile) {
        try (OutputStream archiveStream = IOUtils.buffer(new FileOutputStream(outputZipFile));
             ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, archiveStream);) {
            ZipArchiveEntry entry = new ZipArchiveEntry(inputFile.getName());
            archive.putArchiveEntry(entry);
            try (InputStream input = IOUtils.buffer(new FileInputStream(inputFile));) {
                IOUtils.copy(input, archive);
            } finally {
                archive.closeArchiveEntry();
                archive.finish();
            }
        } catch(IOException | ArchiveException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }

    public static void unzip(File inputZipFile, OutputStream output) {
        try (ZipFile zipFile = new ZipFile(inputZipFile);) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                try (InputStream content = IOUtils.buffer(zipFile.getInputStream(entry));) {
                    IOUtils.copy(content, output);
                }
            }
        } catch(IOException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }
    
    public static File unzipToFile(File inputZipFile) {
        try {
            File tempFile = File.createTempFile("unzip", ".tmp");
            try (ZipFile zipFile = new ZipFile(inputZipFile);
                 OutputStream output = IOUtils.buffer(new FileOutputStream(tempFile));) {
                Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
                while (entries.hasMoreElements()) {
                    ZipArchiveEntry entry = entries.nextElement();
                    try (InputStream content = IOUtils.buffer(zipFile.getInputStream(entry));) {
                        IOUtils.copy(content, output);
                    }
                }
            } catch(IOException e) {
                FileUtils.forceDelete(tempFile);
                throw e;
            }
            return tempFile;
        } catch(IOException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }
    
    public static File ungzToFile(File inputZipFile) {
        try {
            File tempFile = File.createTempFile("unGZ", ".tmp");
            try (InputStream input = IOUtils.buffer(new GZIPInputStream(new FileInputStream(inputZipFile)));
                 OutputStream output = IOUtils.buffer(new FileOutputStream(tempFile));) {
                IOUtils.copy(input, output);
            } catch(IOException e) {
                FileUtils.forceDelete(tempFile);
                throw e;
            }
            return tempFile;
        } catch(IOException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }

    public static String md5Hex(File inputFile) {
        try (InputStream input = IOUtils.buffer(new FileInputStream(inputFile));) {
            if (input.available() == 0) {
                throw new OpdException("檔案沒有內容");
            }
            return md5Hex(input);
        } catch(IOException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }

    public static String md5Hex(InputStream inputStream) {
        try {
            return DigestUtils.md5Hex(inputStream);
        } catch(IOException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }

    /**
     * 取得被壓縮檔的 md5
     * @param zipInputFile
     * @return
     */
    public static String zipEntryMd5Hex(File zipInputFile) {
        File uncompressedFile = unzipToFile(zipInputFile);
        try {
            return md5Hex(uncompressedFile);
        } finally {
            deleteQuietly(uncompressedFile);
        }
    }

    public static boolean deleteQuietly(final File file) {
        return org.apache.commons.io.FileUtils.deleteQuietly(file);
    }
    
    public static File writeByteArrayToFile(final byte[] data) {
        try {
            File tmpFile = File.createTempFile("tempFile", ".tmp");
            org.apache.commons.io.FileUtils.writeByteArrayToFile(tmpFile, data);
            return tmpFile;
        } catch(IOException e) {
            throw new OpdException(ErrorCodeEnum.DEFAULT_EXCEPTION, e);
        }
    }
    
    /**
     * Download file
     * @param url
     * @return
     * @throws RestClientException
     * @throws java.net.URISyntaxException
     */
    public static File featchFile(URL url) throws URISyntaxException {
        final RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            }
        };
        ResponseExtractor<File> responseExtractor = new ResponseExtractor<File>() {
            @Override
            public File extractData(ClientHttpResponse response) throws IOException {
                Path tempFilePath = Files.createTempFile("zipped", ".tmp");
                Files.copy(response.getBody(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                return tempFilePath.toFile();
            }
        };

//        final RestTemplate restTemplate = new RestTemplate();
//        final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build()));
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate.execute(url.toURI(), HttpMethod.GET, requestCallback, responseExtractor);
    }

    public static void forceDelete(final File file) throws IOException {
        org.apache.commons.io.FileUtils.forceDelete(file);
    }

}
