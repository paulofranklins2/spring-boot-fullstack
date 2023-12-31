package com.paulofranklins.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    private S3Service underTest;
    @Mock
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        underTest = new S3Service(s3Client);
    }

    @Test
    void canPutObject() throws IOException {
        //Given
        var bucket = "customer";
        var key = "foo";
        var file = "Hello World".getBytes();
        //When

        underTest.putObject(bucket, key, file);

        //Then
        var putObjectRequestArgumentCaptor =
                ArgumentCaptor.forClass(PutObjectRequest.class);
        var requestBodyArgumentCaptor =
                ArgumentCaptor.forClass(RequestBody.class);

        verify(s3Client).putObject(
                putObjectRequestArgumentCaptor.capture(),
                requestBodyArgumentCaptor.capture());

        var putObjectRequestArgumentCaptorValue =
                putObjectRequestArgumentCaptor.getValue();

        assertThat(
                putObjectRequestArgumentCaptorValue.bucket())
                .isEqualTo(bucket);

        assertThat(
                putObjectRequestArgumentCaptorValue.key())
                .isEqualTo(key);

        var requestBodyArgumentCaptorValue =
                requestBodyArgumentCaptor.getValue();

        assertThat(
                requestBodyArgumentCaptorValue.contentStreamProvider().newStream().readAllBytes())
                .isEqualTo(RequestBody.fromBytes(file).contentStreamProvider().newStream().readAllBytes());
    }

    @Test
    void canGetObject() throws IOException {
        //Given
        var bucket = "customer";
        var key = "foo";
        var file = "Hello World".getBytes();

        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        var res =
                mock(ResponseInputStream.class);
        when(res.readAllBytes())
                .thenReturn(file);

        when(s3Client.getObject(
                eq(getObjectRequest)))
                .thenReturn(res);

        //When
        var bytes = underTest.getObject(bucket, key);

        //Then
        assertThat(bytes).isEqualTo(file);
    }

    @Test
    void willThrowWhenGetObject() throws IOException {
        //Given
        var bucket = "customer";
        var key = "foo";
        var file = "Hello World".getBytes();

        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        var res =
                mock(ResponseInputStream.class);
        when(res.readAllBytes())
                .thenThrow(new IOException("Cannot read bytes"));

        when(s3Client.getObject(
                eq(getObjectRequest)))
                .thenReturn(res);

        //Then
        assertThatThrownBy(() -> underTest.getObject(bucket, key))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot read bytes")
                .hasRootCauseInstanceOf(IOException.class);

    }
}