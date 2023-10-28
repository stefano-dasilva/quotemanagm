# Descrizione del Progetto

Questo progetto è un'applicazione web che consente la gestione di richieste di preventivi per prodotti personalizzati. L'applicazione supporta registrazione e login di clienti e impiegati mediante una pagina pubblica con opportune form. La registrazione controlla l'unicità dello username. Un preventivo è associato a un prodotto, al cliente che l'ha richiesto e all'impiegato che l'ha gestito. Il preventivo comprende una o più opzioni per il prodotto a cui è associato, che devono essere tra quelle disponibili per il prodotto. Un prodotto ha un codice, un'immagine e un nome. Un'opzione ha un codice, un tipo ("normale", "in offerta") e un nome. Il prezzo del preventivo è definito dall'impiegato.

## Funzionalità Principali

## Registrazione e Login 
Gli utenti possono registrarsi e accedere al sistema attraverso una pagina pubblica con form di registrazione e login. La registrazione verifica l'unicità dell'username.

## Home Page Cliente
Quando un cliente fa login, accede a una pagina HOME PAGE CLIENTE che contiene una form per creare un preventivo e l'elenco dei preventivi creati dal cliente. Selezionando uno dei preventivi il cliente ne visualizza i dettagli.

## Creazione di Preventivi
Mediante la form di creazione di un preventivo, l'utente sceglie il prodotto e le relative opzioni. L'utente può confermare l'invio del preventivo.

## Home Page Impiegato
Quando un impiegato effettua il login, accede a una pagina HOME PAGE IMPIEGATO che contiene l'elenco dei preventivi gestiti da lui in precedenza e quelli non ancora associati a nessun impiegato.

## Gestione dei Preventivi da parte dell'Impiegato
L'impiegato può selezionare un preventivo non ancora associato a nessun impiegato e inserire il prezzo del preventivo. Questo prezzo è visibile al cliente.

## Interfaccia Utente Responsiva
L'applicazione offre un'interfaccia utente reattiva, consentendo agli utenti di navigare senza ricaricare completamente la pagina.

## Note Aggiuntive
 Il file [Presentazione.pptx](Presentazione.pptx) contiene lo studio della specifica con il grafico entità relazione e il modello del progetto.

## Logout
L'applicazione consente il logout degli utenti.

## Strumenti utilizzati
- `Linguaggi` : HTML, CSS, JAVA lato server, SQL ( Servlet )
- `Ambiente` : Eclipse, TomCat, MySQLConnector
