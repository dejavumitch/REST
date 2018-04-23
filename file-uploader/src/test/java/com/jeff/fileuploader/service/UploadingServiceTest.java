package com.jeff.fileuploader.service;

import com.jeff.fileuploader.exception.FileNotFoundException;
import com.jeff.fileuploader.exception.FileUploadFailureException;
import com.jeff.fileuploader.module.MetaData;
import com.jeff.fileuploader.repository.PersistService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class UploadingServiceTest {

    @Value("upload.folder.path")
    private String filePathPrefix;

    @Mock
    private PersistService persistService;

    @InjectMocks
    private UploadingService uploadingService = new UploadingService();

    private MetaData metaData;

    private MultipartFile file;

    @Before
    public void setUp() {
        final String fileContent = "mock test";
        metaData = new MetaData("test");
        metaData.setFileSize(fileContent.length());

        file = new MockMultipartFile("file", "test", null, fileContent.getBytes());
    }

    @Test
    public void findMetaDataByNameSuccess() throws FileNotFoundException {
        Mockito.when(uploadingService.findAll()).thenReturn(new ArrayList<MetaData>() {{
            add(metaData);
        }});
        Assert.assertEquals(metaData, uploadingService.findMetaDataByName("test"));
    }

    @Test(expected = FileNotFoundException.class)
    public void findMetaDataByNameFailure() throws FileNotFoundException {
        Mockito.when(persistService.findAll()).thenReturn(new ArrayList<MetaData>());
        Assert.assertEquals(metaData, uploadingService.findMetaDataByName("test"));
    }

    @Test
    public void saveSuccess() throws FileUploadFailureException {
        Mockito.when(persistService.save(metaData)).thenReturn(metaData);
        Assert.assertEquals(metaData.toString(), uploadingService.save(file).toString());
    }

}