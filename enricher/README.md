# ENRICHER


## Funktionsweise der Anwendung

Die Anwendung nimmt eine JSON Objekt entgegen, bildet dies auf eine Java Objekt ab und gibt wiederum den Inhalt auf der Console aus.

## Betrieb/Test der Anwendung

Der ESB wurde mithilfe der rest.json.Application Klasse konfiguriert und kann auch mit dieser Klasse gestartet werden.

Ein JSON Objekte wird unter der [Adresse](http://localhost:8080/foo) entgegengenommen.
Die JSON Nachricht sollte als HTTP POST im Body verschickt werden und folgendes Format einhalten:

{ "id":1, "email":"Test@Test.de", "username":"Test", "password":"Test", "firstname":"TestVorname", "lastname":"TestNachname" }

Dadurch kann das JSON Objekt auf das Java Objekt rest.json.User abgebildet werden. 
Nach der ersten Ausgabe des Payloads, wird eine Header statische Header Variable gesetzt.




 

