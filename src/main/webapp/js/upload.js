// Validates the selected file and gives loading feedback on submit
document.addEventListener("DOMContentLoaded", () => {
    const uploadForm = document.querySelector('form[action$="/upload-note"]');
    const pdfInput  = document.getElementById("pdfFile");
    const submitBtn = uploadForm ? uploadForm.querySelector('button[type="submit"]') : null;

    if (!uploadForm || !pdfInput || !submitBtn) return;

    uploadForm.addEventListener("submit", function (event) {
        const file = pdfInput.files[0];
        if (!file) return;

        // Check both the file extension and the MIME type
        // (octet-stream is included because some browsers report it for PDF files)
        const fileName = file.name.toLowerCase();
        const fileType = file.type;
        const isPdf = fileName.endsWith(".pdf")
            && (!fileType || fileType === "application/pdf" || fileType === "application/octet-stream");

        if (!isPdf) {
            event.preventDefault();
            alert("Only PDF files can be uploaded.");
            pdfInput.value = "";
            return;
        }

        // 3 — loading feedback: disable button and change label
        // This prevents accidental double-submit and clearly signals the upload is in progress.
        submitBtn.disabled = true;
        submitBtn.textContent = "Uploading…";
    });
});
