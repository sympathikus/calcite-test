# Calcite File Example

Dieses Projekt zeigt, wie Apache Calcite genutzt werden kann um SQL-Queries auf JSON- und CSV-Datein auszufuehren.
Weitere Informationen und Tutorials zu Apache Calcite findet man auf ihrer [Homepage](https://calcite.apache.org/). Den dazugehoerigen Source-Code findet man auf [github](https://github.com/apache/calcite).

Die model.json Datei in *src/test/resources* gibt das Verzeichnis an, in dem Apache Calcite nach Dateien sucht. Diese zeigt aktuell auf den Ordner *src/test/resources/test*, der bereits einige Beispieldatein beinhaltet.
Natuerlich kann dieser um eigene JSON- und CSV-Dateien erweitert werden. 

Zum Starten des Beispiels muss die Klasse TestLauncher ausgefuehrt werden. Diese bereitet Apache Calcite mithilfe der bereitgestellten model.json Datei vor und startet ein rudimentaeres Command-Line-Interface.
Nach erfolgreichem Hochfahren kann eine Query eingegeben werden, wobei folgendes zu beachten ist:
- getestet wurden bisher nur SELECT-Statements
- die Tabellen tragen den Namen der entsprechenden CSV/JSON-Datei. Da EXAMPLE.json in SQL kein Tabellenname, sondern ein Zugriff auf die Spalte 'json' der Tabelle 'EXAMPLE' ist, 
muss der Name der Tabelle (also der Datei) mit "" angegeben werden

Eine Beispielquery sieht dann so aus:  

**select * from "STUDENTS.json"**
 



