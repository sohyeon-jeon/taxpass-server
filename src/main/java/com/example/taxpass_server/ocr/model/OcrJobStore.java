package com.example.taxpass_server.ocr.model;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OcrJobStore {

    private final Map<String, OcrJobStatus> store = new ConcurrentHashMap<>();

    public OcrJobStatus create() {
        String jobId = UUID.randomUUID().toString();
        OcrJobStatus status = new OcrJobStatus(jobId, 0, "대기중", null);
        store.put(jobId, status);


        return status;
    }

    public void update(String jobId, int progress, String message) {
        OcrJobStatus status = store.get(jobId);
        if (status != null) {
            status.setProgress(progress);
            status.setMessage(message);
        }
    }

    public OcrJobStatus get(String jobId) {

        return store.get(jobId);
    }

    public void updateWithResult(String jobId, int progress, String message, String result) {
        OcrJobStatus status = store.get(jobId);
        if (status != null) {
            status.setProgress(progress);
            status.setMessage(message);
            status.setResult(result);
        }
    }

}
