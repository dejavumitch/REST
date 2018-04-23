package com.jeff.fileuploader.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff.fileuploader.exception.FileAlreadyExistsException;
import com.jeff.fileuploader.exception.FileNotFoundException;
import com.jeff.fileuploader.module.MetaData;
import com.jeff.fileuploader.service.UploadingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploaderController.class)
public class FileUploaderControllerTest {

    @Value("upload.folder.path")
    private String directory;

    @Mock
    private FileUploaderController uploadController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UploadingService uploadingService;

    private MetaData metaData;

    private MockMultipartFile file;

    @Before
    public void setUp() {
        final String fileContent = "mock test";
        metaData = new MetaData("test");
        metaData.setFileSize(fileContent.length());
        file = new MockMultipartFile("file", "test", null, fileContent.getBytes());
    }


    @Test
    public void getMetaDataSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Mockito.when(uploadingService.findMetaDataByName("test")).thenReturn(metaData);
        mvc.perform(get("/api/file/test"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(metaData)));
    }

    @Test
    public void getMetaDataFailure() throws Exception {
        Mockito.when(uploadingService.findMetaDataByName("test")).thenThrow(FileNotFoundException.class);
        mvc.perform(get("/api/file/test"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void uploadFileSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Mockito.when(uploadingService.save(file)).thenReturn(metaData);
        Mockito.when(uploadingService.findMetaDataByName("test")).thenThrow(FileNotFoundException.class);
        mvc.perform(multipart("/api/file").file(file))
                .andExpect(status().isCreated());
    }

    @Test
    public void uploadFileFailure() throws Exception {
        Mockito.when(uploadingService.save(file)).thenThrow(FileAlreadyExistsException.class);
        mvc.perform(multipart("/api/file").file(file))
                .andExpect(status().isConflict());
    }

    @Configuration
    @ComponentScan(basePackageClasses = {FileUploaderController.class})
    public static class TestConf {
    }
}