INSERT INTO users (email, password, username, name)
VALUES
    ('test@email.com', 'test', 'testuser', 'Test User'),
    ('test2@email.com', 'test', 'pincoPallino', 'Test User'),
    ('test3@email.com', 'test', 'marioRossi', 'Test User'),
    ('test4@email.com', 'test', 'nicolaVerdi', 'Test User'),
    ('test5@email.com', 'test', 'john2000', 'Test User'),
    ('test6@email.com', 'test', 'james2000', 'Test User'),
    ('test7@email.com', 'test', 'testuser10', 'Test User'),
    ('test8@email.com', 'test', 'testuser25', 'Test User');

INSERT INTO course (name)
VALUES
    ('Analisi 1'),
    ('Analisi 2'),
    ('Programmazione'),
    ('Basi di Dati'),
    ('Algoritmi e Strutture Dati'),
    ('Reti di Calcolatori');

INSERT INTO note (author_id, course_id, title, description, file_path)
VALUES
    (3, 1, 'Limiti e continuità', 'Appunti su limiti, continuità e teoremi fondamentali di Analisi 1', '/uploads/limiti.pdf'),

    (3, 1, 'Derivate e applicazioni', 'Regole di derivazione, derivate composte e applicazioni', '/uploads/derivate.pdf'),

    (4, 2, 'Integrali definiti', 'Tecniche di integrazione e integrali definiti e indefiniti', '/uploads/integrali.pdf'),

    (4, 3, 'SQL basi', 'SELECT, INSERT, UPDATE, DELETE e join tra tabelle', '/uploads/sql_basi.pdf'),

    (5, 4, 'Algoritmi di ordinamento', 'Bubble sort, merge sort, quick sort spiegati', '/uploads/sorting.pdf'),

    (5, 4, 'Ricerca binaria', 'Algoritmo di ricerca binaria e complessità', '/uploads/binary_search.pdf'),

    (6, 5, 'TCP IP basics', 'Modello TCP/IP e funzionamento rete internet', '/uploads/tcp_ip.pdf'),

    (6, 3, 'Normalizzazione database', '1NF, 2NF, 3NF e dipendenze funzionali', '/uploads/normalizzazione.pdf');

