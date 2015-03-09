# Strukturierung von Flows 


## Funktionsweise der Anwendung

Die Anwendung zeigt wie Flows mit dem ESB Spring Integration Strukturiert werden können.
Es wurden mehrere Flows definiert, jeder dieser Flows gibt die erhaltene Nachricht weiter.
Durch diese Art Flows zu strukturieren können verwendet werden, um in einem Flow eine bestimmte Aufgabe zu lösen.
Wurde die spezifische Aufgabe von einem Flow erledigt, so kann in einem nachfolgenden Flow eine weitere Aufgabe erledigt werden.
Es ist also nicht notwendig, eine größere Aufgabenstellung innerhalb eines Flows zu lösen.   

## Betrieb/Test der Anwendung

Der ESB wurde mithilfe der rest.json.Application Klasse konfiguriert und kann auch mit dieser Klasse gestartet werden.

Eine Zeichenkette wird unter der [Adresse](http://localhost:8080/foo) entgegengenommen.
Die Nachricht sollte als HTTP POST im Body verschickt werden, der Inhalt kann bei diesem Beispiel beliebig sein.
Es wird gezeigt, wie Nachrichten von einem Flow zu einem anderen Flow übertragen werden.
Innerhalb des Beispiels werden drei Flows durchlaufen:
-requestChannel
-TestChannel1
-TestChannel2
-testXChannel3




 

