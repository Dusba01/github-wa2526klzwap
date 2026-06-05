INSERT INTO users (email, password, username, name)
VALUES
    ('test@email.com',  'test', 'testuser',    'Test User'),
    ('test2@email.com', 'test', 'pincoPallino','Test User'),
    ('test3@email.com', 'test', 'marioRossi',  'Test User'),
    ('test4@email.com', 'test', 'nicolaVerdi', 'Test User'),
    ('test5@email.com', 'test', 'john2000',    'Test User'),
    ('test6@email.com', 'test', 'james2000',   'Test User'),
    ('test7@email.com', 'test', 'testuser10',  'Test User'),
    ('test8@email.com', 'test', 'testuser25',  'Test User');

INSERT INTO course (name)
VALUES
    ('Analisi 1'),
    ('Analisi 2'),
    ('Programmazione'),
    ('Basi di Dati'),
    ('Algoritmi e Strutture Dati'),
    ('Reti di Calcolatori'),
    ('Sistemi Operativi'),
    ('Architettura degli Elaboratori'),
    ('Ingegneria del Software'),
    ('Calcolo delle Probabilità e Statistica'),
    ('Fisica 1'),
    ('Algebra Lineare');

INSERT INTO note (author_id, course_id, title, description, file_path)
VALUES
    -- Analisi 1
    (3, 1, 'Limiti e continuità',
     'Appunti su limiti, continuità e teoremi fondamentali di Analisi 1',
     '/uploads/limiti.pdf'),

    (3, 1, 'Derivate e applicazioni',
     'Regole di derivazione, derivate composte e applicazioni alle funzioni reali',
     '/uploads/derivate.pdf'),

    (4, 1, 'Serie numeriche',
     'Criteri di convergenza: confronto, rapporto, radice e serie di Taylor',
     '/uploads/serie.pdf'),

    -- Analisi 2
    (4, 2, 'Integrali multipli',
     'Integrali doppi e tripli, cambio di variabile, coordinate polari e sferiche',
     '/uploads/integrali_multipli.pdf'),

    (3, 2, 'Equazioni differenziali',
     'EDO del primo e secondo ordine, metodo di variazione delle costanti',
     '/uploads/edo.pdf'),

    -- Programmazione
    (5, 3, 'Programmazione orientata agli oggetti',
     'Classi, oggetti, ereditarietà, polimorfismo e incapsulamento in Java',
     '/uploads/oop.pdf'),

    (6, 3, 'Strutture dati in C',
     'Liste concatenate, pile, code e alberi implementati in C',
     '/uploads/strutture_c.pdf'),

    -- Basi di Dati
    (4, 4, 'SQL basi',
     'SELECT, INSERT, UPDATE, DELETE e join tra tabelle',
     '/uploads/sql_basi.pdf'),

    (5, 4, 'Normalizzazione',
     '1NF, 2NF, 3NF e dipendenze funzionali con esempi pratici',
     '/uploads/normalizzazione.pdf'),

    (6, 4, 'Modello ER',
     'Entità, relazioni, attributi e traduzione al modello relazionale',
     '/uploads/modello_er.pdf'),

    -- Algoritmi e Strutture Dati
    (3, 5, 'Algoritmi di ordinamento',
     'Bubble sort, merge sort, quick sort: analisi e complessità',
     '/uploads/sorting.pdf'),

    (4, 5, 'Ricerca binaria e grafi',
     'Ricerca binaria, BFS e DFS su grafi con esempi',
     '/uploads/grafi.pdf'),

    (5, 5, 'Programmazione dinamica',
     'Memoization, bottom-up e problemi classici: zaino, LCS',
     '/uploads/dp.pdf'),

    -- Reti di Calcolatori
    (6, 6, 'Modello TCP/IP',
     'Stack protocollare, indirizzamento IP e funzionamento della rete internet',
     '/uploads/tcp_ip.pdf'),

    (3, 6, 'Protocolli applicativi',
     'HTTP, DNS, SMTP e FTP: funzionamento e formato dei messaggi',
     '/uploads/protocolli.pdf'),

    -- Sistemi Operativi
    (4, 7, 'Gestione dei processi',
     'Scheduling CPU, stati di un processo e context switch',
     '/uploads/processi.pdf'),

    (5, 7, 'Memoria virtuale',
     'Paginazione, segmentazione, page fault e algoritmi di sostituzione',
     '/uploads/memoria.pdf'),

    (6, 7, 'Filesystem e I/O',
     'Struttura del filesystem, inode, gestione dei dispositivi e buffer cache',
     '/uploads/filesystem.pdf'),

    -- Architettura degli Elaboratori
    (3, 8, 'Pipeline e parallelismo',
     'Pipeline a 5 stadi, hazard strutturali, dati e controllo',
     '/uploads/pipeline.pdf'),

    (4, 8, 'Memoria cache',
     'Cache a mappatura diretta, associativa e set-associativa, politiche di rimpiazzo',
     '/uploads/cache.pdf'),

    -- Ingegneria del Software
    (5, 9, 'Design pattern',
     'Pattern creazionali, strutturali e comportamentali con esempi in Java',
     '/uploads/design_pattern.pdf'),

    (6, 9, 'UML e modellazione',
     'Diagrammi delle classi, sequenza, casi duso e activity diagram',
     '/uploads/uml.pdf'),

    -- Calcolo delle Probabilità e Statistica
    (3, 10, 'Variabili aleatorie',
     'Distribuzioni discrete e continue: binomiale, Poisson, normale, esponenziale',
     '/uploads/variabili_aleatorie.pdf'),

    (4, 10, 'Inferenza statistica',
     'Stima puntuale, intervalli di confidenza e test delle ipotesi',
     '/uploads/inferenza.pdf'),

    -- Fisica 1
    (5, 11, 'Meccanica classica',
     'Leggi di Newton, lavoro, energia cinetica e potenziale, urti',
     '/uploads/meccanica.pdf'),

    (6, 11, 'Termodinamica',
     'Principi della termodinamica, ciclo di Carnot e gas ideali',
     '/uploads/termodinamica.pdf'),

    -- Algebra Lineare
    (3, 12, 'Spazi vettoriali e basi',
     'Dipendenza lineare, basi, dimensione e cambi di base',
     '/uploads/spazi_vettoriali.pdf'),

    (4, 12, 'Autovalori e diagonalizzazione',
     'Polinomio caratteristico, autovettori e diagonalizzazione di matrici',
     '/uploads/autovalori.pdf');