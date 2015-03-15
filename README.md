# Spring Integration Beispiele

Das Repository enthält diverse Beispiele und zeigt wie Enterprise Integration Patterns mithilfe von Spring Integration realisiert werden können.
Die Konfiguration des ESB kann auf verschiedene Arten realisiert werden, innerhalb der Beispiele wurde ausschließlich die Java DSL verwendet. 

# Einarbeitung in die Beispiel

Da die Beispiele bezüglich Umfang aufeinander Aufbauen wird im folgenden eine Reihenfolge empfohlen um sich in die Beispiele einzuarbeiten:
- simple-flow-lambda
- simple-flow
- rest-json
- structering-flows
- filter
- filter2
- recipientList
- enricher

## Testen der Beispiele

Die Anwendungen nehmen Daten im JSON Format über eine HTTP Adresse entgegen.
Es wird ein REST Client benötigt:
http://restclient.net/
https://code.google.com/p/rest-client/downloads/list
http://code.fosshub.com/WizToolsorg-RESTClient/downloads

Die Daten die dem ESB gesendet werden, werden je nach Beispiel von verschiedenen Patterns verarbeitet und auf der Console ausgegeben.

##Weitere Beispiele

- http://spring.io/blog/2014/11/25/spring-integration-java-dsl-line-by-line-tutorial
- https://spring.io/blog/2014/10/31/spring-integration-java-dsl-1-0-rc1-released
- https://github.com/spring-projects/spring-integration-java-dsl/wiki/Spring-Integration-Java-DSL-Reference