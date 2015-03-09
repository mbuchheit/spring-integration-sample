# Filter


## Funktionsweise der Anwendung

Die Anwendung nimmt eine Zeichenkette entgegen

## Betrieb/Test der Anwendung

Der ESB wurde mithilfe der rest.json.Application Klasse konfiguriert und kann auch mit dieser Klasse gestartet werden.

Eine Zeichenkette wird unter der [Adresse](http://localhost:8080/foo) entgegengenommen.
Die Nachricht sollte per HTTP POST im Body verschickt werden und folgenden Inhalt einhalten:

Test

Hat die Nachricht nicht den Inhalt "Test" so wird diese verworfen.




 

