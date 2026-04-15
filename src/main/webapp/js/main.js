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
                card.style.border = "1px solid #ccc";
                card.style.margin = "10px";
                card.style.padding = "10px";
                card.style.borderRadius = "10px";

                card.innerHTML = `
                    <h3>${note.title}</h3>
                    <p>${note.description}</p>

                    <small>📚 Corso: ${note.courseName}</small><br>
                    <small>👤 Autore: ${note.authorUsername}</small>
                `;

                container.appendChild(card);
            });
        })
        .catch(err => {
            console.error("Errore:", err);
        });
}