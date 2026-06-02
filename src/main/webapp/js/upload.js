document.addEventListener("DOMContentLoaded", () => {
    const uploadForm = document.querySelector('form[action$="/upload-note"]');
    const pdfInput = document.getElementById("pdfFile");

    if (!uploadForm || !pdfInput) return;

    uploadForm.addEventListener("submit", function (event) {
        const file = pdfInput.files[0];
        if (!file) return;

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