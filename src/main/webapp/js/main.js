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

                    <div class="rating" id="rating-${note.id}">
                         ⏳ loading rating...
                    </div>

                    <div class="card-actions">
                        <a class="download-btn" href="${BASE_URL}/download-note?id=${note.id}">⬇ Download PDF</a>
                    </div>
                `;

                container.appendChild(card);
                loadRating(note.id);
            });
        })
        .catch(err => {
            console.error("Errore:", err);
        });
}

function loadRating(noteId) {

    fetch(BASE_URL + "/rest/ratings/" + noteId)
        .then(res => res.json())
        .then(data => {

            const container = document.getElementById("rating-" + noteId);

            const avg = data.average || 0;
            const count = data.count || 0;
            const userValue = data.userValue;

            container.innerHTML = `
                <div>
                    ⭐ ${avg.toFixed(1)} (${count})
                </div>
                <div>
                    ${renderStars(noteId, userValue)}
                </div>
            `;
        });
}

function renderStars(noteId, userValue) {

    let html = "";

    for (let i = 1; i <= 5; i++) {

        const filled = userValue && i <= userValue ? "★" : "☆";

        html += `
            <span style="cursor:pointer; font-size:18px"
                  onclick="rateNote(${noteId}, ${i}, ${userValue || 0})">
                ${filled}
            </span>
        `;
    }

    return html;
}

function rateNote(noteId, value, currentValue) {

    // CASO 1: clicco stesso voto → rimuovo rating
    if (currentValue === value) {

        fetch(BASE_URL + "/rest/ratings/" + noteId, {
            method: "DELETE"
        })
        .then(() => loadRating(noteId));

        return;
    }

    // CASO 2: nuovo voto o cambio voto
    fetch(BASE_URL + "/rest/ratings/" + noteId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ value })
    })
    .then(() => loadRating(noteId));
}
