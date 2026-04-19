console.log("JS caricato");
function searchNotes() {

    const query = document.getElementById("query").value;

    fetch(BASE_URL + "/rest/notes/search?query=" + encodeURIComponent(query))
        .then(res => res.json())
        .then(data => {

            const container = document.getElementById("results");
            container.innerHTML = "";

            data.forEach(note => {

                const card = document.createElement("div");
                card.className = "card";

                card.innerHTML = `
                    <h3>${note.title}</h3>
                    <p>${note.description || "No description available."}</p>
                    <div class="card-meta">
                        <small>📚 Corso: ${note.courseName}</small>
                        <small>👤 Autore: ${note.authorUsername}</small>
                    </div>
                    <div class="card-actions">
                        <a class="download-btn" href="${BASE_URL}/download-note?id=${note.id}">⬇ Download PDF</a>
                    </div>
                `;

                container.appendChild(card);
            });
        })
        .catch(err => {
            console.error("Errore:", err);
        });
}
