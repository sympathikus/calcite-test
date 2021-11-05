# calcite-test
Zum Starten die Klasse TestLauncher ausführen. Diese beinhaltet auch die durchgeführten Queries, also ist dies die erste Anlaufstelle, wenn ihr
einfach mal damit rumspielen wollt. 
Zudem befinden sich im Verzeichnis "src/main/resources" zwei verschieden model-files, die unterschiedliche Tabellen auf Basis verschiedener Dateiverzeichnissen aufbauen.
Die entsprechenden Daten finden sich unter "src/main/resources/test" und "src/main/resources/sales-json".

Wer sich anschauen möchte, welche Schritte notwendig sind um einen eigenen Adapter zu schreiben, dem kann ich sowohl das Tutorial auf https://calcite.apache.org/docs/tutorial.html,
als auch den Source-Code dazu https://github.com/apache/calcite/tree/master/example/csv ans Herz legen. 
Unter https://calcite.apache.org/docs/tutorial.html findet man den CSV-Adapter, an dem die wichtigsten Schritte zum implementieren eines eigenen Adapters beschrieben werden.
Wer sich die anderen, auf https://calcite.apache.org/docs/adapter.html erwähnten Adapter anschauen möchte, der findet diese hier: https://github.com/apache/calcite. 
Insbesondere der FileAdapter könnte für unseren Fall von Interesse sein.




