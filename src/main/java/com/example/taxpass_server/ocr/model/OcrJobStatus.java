package com.example.taxpass_server.ocr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OcrJobStatus {
    private String jobId;
    private int progress;     // 0~100
    private String message;   // 현재 단계

    private Object result;
}
