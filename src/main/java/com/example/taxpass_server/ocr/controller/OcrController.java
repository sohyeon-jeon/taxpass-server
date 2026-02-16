package com.example.taxpass_server.ocr.controller;

import com.example.taxpass_server.ocr.model.OcrJobStatus;
import com.example.taxpass_server.ocr.model.OcrJobStore;
import com.example.taxpass_server.ocr.service.OcrWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ocr")
public class OcrController {

    private final OcrJobStore jobStore;
    private final OcrWorkerService workerService;

    @GetMapping("/ping")
    public String ping() {
        return "ocr ok";
    }

    @PostMapping("/job")
    public OcrJobStatus createJob() {
        return jobStore.create();
    }

    @PostMapping("/upload")
    public Map<String, String> uploadPdf(@RequestParam("file") MultipartFile file, @RequestParam("startPage") int startPage, @RequestParam("endPage") int endPage) throws Exception {

        // job 생성
        OcrJobStatus job = jobStore.create();

        // 임시 파일 생성
        Path pdfPath = Files.createTempFile("ocr-", ".pdf");
        file.transferTo(pdfPath);

        //  Python worker 실행 (페이지 전달)
        workerService.runPython(job.getJobId(), pdfPath, startPage, endPage);

        return Map.of("jobId", job.getJobId());
    }


    // Polling 전용 progress API
    @GetMapping("/progress/{jobId}")
    public OcrJobStatus progress(@PathVariable String jobId) {

        OcrJobStatus status = jobStore.get(jobId);

        if (status == null) {
            // 없는 job 처리
            return new OcrJobStatus(jobId, 100, "존재하지 않는 작업입니다.", null);
        }

        return status;
    }
}
