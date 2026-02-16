import sys
import time

job_id = sys.argv[1]
pdf_path = sys.argv[2]

def report(progress, message):
    print(f"PROGRESS:{progress}:{message}", flush=True)

report(5, "PDF 로딩")
time.sleep(1)

report(20, "이미지 전처리")
time.sleep(1)

report(50, "OCR 수행")
time.sleep(1)

report(80, "OpenAI 호출")
time.sleep(1)

report(100, "완료")
