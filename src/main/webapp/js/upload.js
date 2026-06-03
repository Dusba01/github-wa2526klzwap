// Validates the selected file before the upload form is submitted
document.addEventListener("DOMContentLoaded", () => {
    const uploadForm = document.querySelector('form[action$="/upload-note"]');
    const pdfInput = document.getElementById("pdfFile");

    if (!uploadForm || !pdfInput) return;

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
        }
    });
});