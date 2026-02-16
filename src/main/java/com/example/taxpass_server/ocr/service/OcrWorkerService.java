package com.example.taxpass_server.ocr.service;

import com.example.taxpass_server.ocr.model.OcrJobStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class OcrWorkerService {

    private final OcrJobStore jobStore;

    public void runPython(
            String jobId,
            Path pdfPath,
            int startPage,
            int endPage
    ) {

        new Thread(() -> {
            System.out.println("Python worker start: " + jobId);

            try {
                ClassLoader classLoader = getClass().getClassLoader();

                // worker.py
                File workerFile = new File(
                        classLoader.getResource("python/worker.py").getFile()
                );

                // venv python 경로
                File venvPython = new File(
                        classLoader.getResource("python/.venv/bin/python").getFile()
                );

                String workerPath = workerFile.getAbsolutePath();
                String pythonExec = venvPython.getAbsolutePath();

                System.out.println("python exec = " + pythonExec);
                System.out.println("worker.py  = " + workerPath);
                System.out.println("pdf path  = " + pdfPath.toAbsolutePath());

                ProcessBuilder pb = new ProcessBuilder(
                        pythonExec,
                        workerPath,
                        jobId,
                        pdfPath.toString(),
                        String.valueOf(startPage),
                        String.valueOf(endPage)

                );

                // stderr + stdout 합치기
                pb.redirectErrorStream(true);

                Process process = pb.start();

                try (BufferedReader reader =
                             new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("python: " + line);

                        if (line.startsWith("PROGRESS:")) {
                            String[] parts = line.split(":", 3);
                            jobStore.update(
                                    jobId,
                                    Integer.parseInt(parts[1]),
                                    parts[2]
                            );
                        }
                    }
                }

                // Python 종료 대기
                process.waitFor();

                // JSON 파일 읽기
                Path jsonPath = Path.of(
                        "data/ox_question/ox_json",
                        jobId + ".json"
                );

                if (jsonPath.toFile().exists()) {

                    String json = java.nio.file.Files.readString(jsonPath);

                    jobStore.updateWithResult(
                            jobId,
                            100,
                            "완료",
                            json
                    );

                    System.out.println("JSON 로드 완료");
                } else {
                    jobStore.update(jobId, 100, "JSON 파일 없음");
                }

                System.out.println("Python worker finished: " + jobId);


            } catch (Exception e) {
                e.printStackTrace();
                jobStore.update(jobId, 100, "실패");
            }
        }).start();


    }
}
