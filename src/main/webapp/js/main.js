console.log("JS caricato");
document.addEventListener("DOMContentLoaded", () => {
    loadCourseSummaries();
});

function searchNotes() {

    const query = document.getElementById("query").value;
    const courseStrip = document.getElementById("courseStrip");

    if (courseStrip) {
        courseStrip.style.display = "none";
    }

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
                        <div class="card-actions-left">
                            <button
                                type="button"
                                class="favorite-btn ${note.isFavorite ? "active" : ""}"
                                data-note-id="${note.id}"
                                data-favorite="${note.isFavorite}"
                                aria-label="${note.isFavorite ? "Remove from favorites" : "Add to favorites"}"
                                title="${note.isFavorite ? "Remove from favorites" : "Add to favorites"}"
                            >${note.isFavorite ? "♥" : "♡"}</button>
                        </div>
                        <a class="download-btn" href="${BASE_URL}/download-note?id=${note.id}">⬇ Download PDF</a>
                    </div>
                `;

                container.appendChild(card);
                loadRating(note.id);
            });

            bindFavoriteButtons();
        })
        .catch(err => {
            console.error("Errore:", err);
        });
}

function loadCourseSummaries() {
    fetch(BASE_URL + "/rest/courses/summary")
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("courseStrip");
            if (!container) {
                return;
            }

            container.innerHTML = "";

            data.forEach(course => {
                const box = document.createElement("button");
                box.type = "button";
                box.className = "course-box";
                box.innerHTML = `
                    <div class="course-box-title">${course.name}</div>
                    <div class="course-box-count">${course.documentCount} document${course.documentCount === 1 ? "" : "s"}</div>
                `;
                box.addEventListener("click", () => {
                    const queryInput = document.getElementById("query");
                    queryInput.value = course.name;
                    searchNotes();
                });
                container.appendChild(box);
            });
        })
        .catch(err => {
            console.error("Errore caricamento corsi:", err);
        });
}

function bindFavoriteButtons() {
    document.querySelectorAll(".favorite-btn").forEach(button => {
        button.addEventListener("click", async () => {
            const noteId = button.dataset.noteId;
            const isFavorite = button.dataset.favorite === "true";
            const method = isFavorite ? "DELETE" : "POST";

            try {
                const response = await fetch(`${BASE_URL}/rest/favorites/${noteId}`, {
                    method
                });

                if (!response.ok) {
                    throw new Error("Favorite toggle failed");
                }

                const nextFavorite = !isFavorite;
                button.dataset.favorite = String(nextFavorite);
                button.textContent = nextFavorite ? "♥" : "♡";
                button.classList.toggle("active", nextFavorite);
                button.setAttribute("aria-label", nextFavorite ? "Remove from favorites" : "Add to favorites");
                button.setAttribute("title", nextFavorite ? "Remove from favorites" : "Add to favorites");
            } catch (error) {
                console.error("Errore preferiti:", error);
            }
        });
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
