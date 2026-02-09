// Upload form handler
const uploadForm = document.getElementById("upload-form");
if (uploadForm) {
    uploadForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const fileInput = document.getElementById("video-file");
        const intervalInput = document.getElementById("frame-interval");
        const uploadBtn = document.getElementById("upload-btn");
        const statusEl = document.getElementById("upload-status");
        const progressContainer = document.getElementById("progress-bar-container");
        const progressFill = document.getElementById("progress-fill");
        const progressText = document.getElementById("progress-text");

        if (!fileInput.files.length) return;

        uploadBtn.disabled = true;
        statusEl.style.display = "none";
        progressContainer.style.display = "block";
        progressText.textContent = "Uploading...";
        progressFill.style.width = "0%";

        // Step 1: Upload
        const formData = new FormData();
        formData.append("file", fileInput.files[0]);

        try {
            const xhr = new XMLHttpRequest();
            const uploadPromise = new Promise((resolve, reject) => {
                xhr.upload.addEventListener("progress", (e) => {
                    if (e.lengthComputable) {
                        const pct = Math.round((e.loaded / e.total) * 100);
                        progressFill.style.width = pct + "%";
                        progressText.textContent = `Uploading... ${pct}%`;
                    }
                });
                xhr.addEventListener("load", () => {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        resolve(JSON.parse(xhr.responseText));
                    } else {
                        reject(new Error(`Upload failed: ${xhr.statusText}`));
                    }
                });
                xhr.addEventListener("error", () => reject(new Error("Upload failed")));
                xhr.open("POST", "/api/upload");
                xhr.send(formData);
            });

            const uploadResult = await uploadPromise;
            progressFill.style.width = "100%";
            progressText.textContent = "Upload complete. Starting decomposition...";

            // Step 2: Start decompose job
            const decomposeResp = await fetch("/api/decompose", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    source_video_id: uploadResult.id,
                    frame_interval: parseFloat(intervalInput.value) || 1.0,
                }),
            });

            if (!decomposeResp.ok) {
                throw new Error("Failed to start decomposition");
            }

            const jobResult = await decomposeResp.json();
            statusEl.className = "status-message success";
            statusEl.textContent = "Job started! Redirecting...";
            statusEl.style.display = "block";

            // Redirect to job page
            setTimeout(() => {
                window.location.href = `/job/${jobResult.job_id}`;
            }, 1000);
        } catch (err) {
            statusEl.className = "status-message error";
            statusEl.textContent = err.message;
            statusEl.style.display = "block";
            uploadBtn.disabled = false;
            progressContainer.style.display = "none";
        }
    });
}

// Decompose from dashboard
function startDecompose(videoId) {
    const interval = prompt("Frame sample interval in seconds (default: 1.0):", "1.0");
    if (interval === null) return;

    fetch("/api/decompose", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            source_video_id: videoId,
            frame_interval: parseFloat(interval) || 1.0,
        }),
    })
        .then((r) => r.json())
        .then((data) => {
            window.location.href = `/job/${data.job_id}`;
        })
        .catch((err) => alert("Error: " + err.message));
}

// Auto-refresh job page when processing
const processingNotice = document.getElementById("processing-notice");
if (processingNotice) {
    const jobId = processingNotice.dataset.jobId;
    const pollInterval = setInterval(async () => {
        try {
            const resp = await fetch(`/api/jobs/${jobId}`);
            const data = await resp.json();
            if (data.job.status === "complete" || data.job.status === "failed") {
                clearInterval(pollInterval);
                window.location.reload();
            }
        } catch {
            // ignore polling errors
        }
    }, 3000);
}
