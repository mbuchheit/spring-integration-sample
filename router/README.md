# Router


## Funktionsweise der Anwendung

Die Anwendung nimmt eine JSON Objekt entgegen, bildet dies auf eine Java Objekt ab und gibt wiederum den Inhalt auf der Console aus.

## Betrieb/Test der Anwendung

Der ESB wurde mithilfe der rest.json.Application Klasse konfiguriert und kann auch mit dieser Klasse gestartet werden.

Ein HTTP Nachricht wird unter der [Adresse](http://localhost:8080/foo) entgegengenommen.
Enthält diese "test", so wird die Nachricht an den testChannel weitergeleitet.
Enthält die Nachricht nicht "test" so wird diese an den garbageChannel weitergeleitet.





 

