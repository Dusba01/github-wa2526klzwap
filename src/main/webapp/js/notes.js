// On page load, populate the course summary strip
document.addEventListener("DOMContentLoaded", () => {
    loadCourseSummaries();
});

// Fetches notes matching the search query and renders them as cards
function searchNotes() {
    const query = document.getElementById("query").value;
    const courseStrip = document.getElementById("courseStrip");

    // Hide the course strip while showing search results
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
                card.innerHTML = buildNoteCardHTML(note);
                container.appendChild(card);
                loadRating(note.id);
            });

            bindFavoriteButtons();
        })
        .catch(err => {
            console.error("Error:", err);
        });
}

// Builds and returns the HTML string for a single note card
function buildNoteCardHTML(note) {
    return `
        <h3>${note.title}</h3>
        <p>${note.description || "No description available."}</p>
        <div class="card-meta">
            <small>📚 Course: ${note.courseName}</small>
            <small>👤 Author: ${note.authorUsername}</small>
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
}

// Fetches course summaries and renders them as clickable boxes in the course strip
function loadCourseSummaries() {
    fetch(BASE_URL + "/rest/courses/summary")
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("courseStrip");
            if (!container) return;

            container.innerHTML = "";

            data.forEach(course => {
                const box = document.createElement("button");
                box.type = "button";
                box.className = "course-box";
                box.innerHTML = `
                    <div class="course-box-title">${course.name}</div>
                    <div class="course-box-count">${course.documentCount} document${course.documentCount === 1 ? "" : "s"}</div>
                `;

                // Clicking a course box pre-fills the search query and runs a search
                box.addEventListener("click", () => {
                    document.getElementById("query").value = course.name;
                    searchNotes();
                });
                container.appendChild(box);
            });
        })
        .catch(err => {
            console.error("Error loading courses:", err);
        });
}

// Attaches click handlers to all favorite buttons currently in the DOM
function bindFavoriteButtons() {
    document.querySelectorAll(".favorite-btn").forEach(button => {
        button.addEventListener("click", async () => {
            const noteId = button.dataset.noteId;
            const isFavorite = button.dataset.favorite === "true";
            const method = isFavorite ? "DELETE" : "POST";

            try {
                const response = await fetch(BASE_URL + "/rest/favorites/" + noteId, { method });

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
                console.error("Favorites error:", error);
            }
        });
    });
}

// Fetches the rating data for a note and renders the average score and star selector
function loadRating(noteId) {
    fetch(BASE_URL + "/rest/ratings/" + noteId)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("rating-" + noteId);
            const avg = data.average || 0;
            const count = data.count || 0;
            const userValue = data.userValue;

            container.innerHTML = `
                <div>⭐ ${avg.toFixed(1)} (${count})</div>
                <div>${renderStars(noteId, userValue)}</div>
            `;
        });
}

// Builds the 5-star HTML for a note, highlighting the stars up to the user's current rating
function renderStars(noteId, userValue) {
    let html = "";

    for (let i = 1; i <= 5; i++) {
        const filled = userValue && i <= userValue ? "★" : "☆";
        html += `<span class="star" data-note-id="${noteId}" data-value="${i}" data-current="${userValue || 0}">${filled}</span>`;
    }

    return html;
}

// Sends a rating request for a note; clicking the same value removes the rating
function rateNote(noteId, value, currentValue) {

    // Case 1: same star clicked again -> remove the existing rating
    if (currentValue === value) {
        fetch(BASE_URL + "/rest/ratings/" + noteId, { method: "DELETE" })
            .then(() => loadRating(noteId));
        return;
    }

    // Case 2: new rating or update to a different value
    fetch(BASE_URL + "/rest/ratings/" + noteId, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ value })
    }).then(() => loadRating(noteId));
}

// Global click listener for star ratings
// reads note id, star value, and current rating from data attributes
document.addEventListener("click", (e) => {
    if (e.target.classList.contains("star")) {
        const noteId = parseInt(e.target.dataset.noteId);
        const value = parseInt(e.target.dataset.value);
        const current = parseInt(e.target.dataset.current);
        rateNote(noteId, value, current);
    }
});