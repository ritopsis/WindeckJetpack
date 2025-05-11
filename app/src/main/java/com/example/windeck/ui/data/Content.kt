package com.example.windeck.ui.data

import com.example.windeck.ui.models.Chapter
import com.example.windeck.ui.models.Question
import java.util.Locale // Für toLowerCase() in älteren Kotlin-Versionen oder plattformübergreifend

object Content {

    var subject: String = ""
    var chapter: Double = 0.0
    var start: Boolean = false
    val contentOfChapter: MutableList<String> = mutableListOf()
    val question: MutableList<Question> = mutableListOf()
    val chapters: MutableList<Chapter> = mutableListOf()

    fun loadChapterOverview() {
        when (subject.lowercase()) {
            "math" -> loadMathChapterOverview()
            "java" -> loadJavaChapterOverview()
        }
    }

    fun loadMathChapterOverview() {
        chapters.clear()
        chapters.add(Chapter(1.0, "Komplexe Zahlen", "Einführung in komplexe Zahlen"))
        chapters.add(Chapter(1.1, "Komplexe Zahlen", "Darstellung im Gaußschen Zahlenraum"))
        chapters.add(Chapter(1.2, "Komplexe Zahlen", "Grundrechenarten mit komplexen Zahlen"))
        chapters.add(Chapter(2.0, "Vektoren", "Grundlagen der Vektoren"))
        chapters.add(Chapter(2.1, "Vektoren", "Skalarprodukt und Vektorenlänge"))
        chapters.add(Chapter(2.2, "Vektoren", "Vektorielle Operationen in der Ebene"))
        chapters.add(Chapter(3.0, "Matrizen", "Einführung in Matrizen"))
        chapters.add(Chapter(3.1, "Matrizen", "Matrixmultiplikation und Determinanten"))
        chapters.add(Chapter(3.2, "Matrizen", "Inverse und transponierte Matrizen"))
    }

    fun loadJavaChapterOverview() {
        chapters.clear()
        chapters.add(Chapter(1.0, "Variablen", "Einführung in Variablen"))
        chapters.add(Chapter(1.1, "Variablen", "Datentypen in Java"))
        chapters.add(Chapter(1.2, "Variablen", "Deklaration und Initialisierung"))
        chapters.add(Chapter(2.0, "Schleifen", "Einführung in Schleifen"))
        chapters.add(Chapter(2.1, "Schleifen", "For-Schleifen und While-Schleifen"))
        chapters.add(Chapter(2.2, "Schleifen", "Verschachtelte Schleifen"))
        chapters.add(Chapter(3.0, "OOP-Grundlagen", "Was ist Objektorientierung?"))
        chapters.add(Chapter(3.1, "OOP-Grundlagen", "Klassen und Objekte"))
        chapters.add(Chapter(3.2, "OOP-Grundlagen", "Methoden und Attribute"))
    }

    fun startLoadingQuestions(subject: String) {
        when (subject.lowercase()) {
            "math" -> loadMathQuestion()
            "java" -> loadJavaQuestion()
            else -> {
                println("Subject $subject is currently not available")
            }
        }
    }

    fun loadMathQuestion() {
        question.clear()
        // Komplexe Zahlen
        question.add(Question(
            topic = "Komplexe Zahlen",
            question = "Eine komplexe Zahl wird allgemein z=x+iy geschrieben. Was bedeuten die Größen x und y in dieser Darstellung?",
            allAnwsers = listOf("x ist der Betrag und y ist das Argument", "x ist der Realteil und y ist der Imaginärteil", "x ist das Argument und y ist der Betrag", "x ist die imaginäre Einheit und y der Realteil")
        ))
        question.add(Question(
            topic = "Komplexe Zahlen",
            question = "Betrachte die komplexe Zahl z=3−2i. In welchem Quadranten der komplexen Ebene befindet sich z?",
            allAnwsers = listOf("Erster Quadrant (rechts oben)", "Zweiter Quadrant (links oben)", "Dritter Quadrant (links unten)", " Vierter Quadrant (rechts unten)")
        ))
        // Vektoren
        question.add(Question(
            topic = "Vektoren",
            question = "Was bedeutet es für zwei Vektoren u und v (Reellen Zahlen), wenn sie orthogonal zueinander sind?",
            allAnwsers = listOf("Ihr Skalarprodukt ist null", "Beide Vektoren haben dieselbe Länge", "Ihr Skalarprodukt ist gleich 1", "Sie liegen auf einer gemeinsamen Geraden")
        ))
        question.add(Question(
            topic = "Vektoren",
            question = "Was ist geometrisch mit „Länge“ eines Vektors v=(x,y) (Reellen Zahlen) gemeint?",
            allAnwsers = listOf("Die Summe der Koordinaten", "Die Differenz der Koordinaten", "Das Produkt der Koordinaten", "Der Abstand vom Ursprung")
        ))
        // Matrizen
        question.add(Question(
            topic = "Matrizen",
            question = "Betrachten Sie eine 2×2-Matrix A. Was kann man über ihre Invertierbarkeit aussagen, wenn det(A)=0?",
            allAnwsers = listOf("Die Matrix ist invertierbar", "Die Matrix ist nicht invertierbar", "Die Matrix hat unendlichen Rang", "Die Matrix ist nur invertierbar, wenn sie symmetrisch ist")
        ))
        question.add(Question(
            topic = "Matrizen",
            question = "Eine reelle n×n-Matrix A heißt symmetrisch, wenn …",
            allAnwsers = listOf("A(Transponiert)=A", "alle Einträge auf der Hauptdiagonale gleich sind", "A(Transponiert)=-A", "alle Einträge über und unter der Diagonale jeweils Gegenzahlen sind")
        ))
    }

    fun loadJavaQuestion() {
        question.clear()
        // Variablen
        question.add(Question(
            topic = "Variablen",
            question = "Welche der folgenden Aussagen über Variablendeklarationen in Java ist korrekt?",
            allAnwsers = listOf("Eine Variable kann ohne Datentyp erstellt werden", "Eine Variable vom Typ int speichert Fließkommazahlen", "Eine Variable muss immer einen Datentyp und eine Bezeichnung haben", "Eine Variable muss immer mit einer Zahl beginnen")
        ))
        question.add(Question(
            topic = "Variablen",
            question = "Wie deklariert man in Java eine Ganzzahl-Variable mit dem Namen anzahl?",
            allAnwsers = listOf("int anzahl;", "anzahl = int;", "anzahl int;", "anzahl = 3")
        ))
        // Schleifen
        question.add(Question(
            topic = "Schleifen",
            question = "Welche Schleife in Java führt den Code mindestens einmal aus, bevor die Bedingung erneut geprüft wird?",
            allAnwsers = listOf("for-Schleife", "while-Schleife", "do-while-Schleife", "if-Abfrage")
        ))
        question.add(Question(
            topic = "Schleifen",
            question = "Welche Schleife ist besonders geeignet, wenn man den Codeblock eine feste Anzahl von Durchläufen (z. B. genau 5 Mal) ausführen möchte?",
            allAnwsers = listOf("while-Schleife", "do-while-Schleife", "for-Schleife", "switch-Anweisung")
        ))
        // OOP-Grundlagen
        question.add(Question(
            topic = "OOP-Grundlagen",
            question = "Was beschreibt eine Klasse in Java?",
            allAnwsers = listOf("Ein bestimmtes Schlüsselwort im Code", "Eine Vorlage (Bauplan) für Objekte", "Nur statische Konstanten", "Eine zufällige Datensammlung ohne Funktion")
        ))
        question.add(Question(
            topic = "OOP-Grundlagen",
            question = "Was ist ein Konstruktor in Java?",
            allAnwsers = listOf("Eine Methode, die nur bei Fehlern aufgerufen wird", "Eine statische Methode, die einen Codeblock ausführt", "Eine spezielle Methode, die beim Erzeugen eines Objekts ausgeführt wird", "Ein Datentyp für Zahlen")
        ))
    }

    fun fullSubjectName(): String {
        return when (subject.lowercase()) {
            "math" -> "Mathe"
            "java" -> "Java"
            else -> ""
        }
    }
    fun getStandardSequence(): String {
        return "1;2;3"
    }

    fun loadChapter() {
        question.clear()
        contentOfChapter.clear()

        val chapterIntPart = chapter.toInt()

        when (subject.lowercase()) {
            "math" -> {
                when (chapterIntPart) {
                    1 -> { // Kapitel 1, 1.1, 1.2, 1.3 etc.
                        contentOfChapter.add("Komplexe Zahlen erweitern das Zahlensystem, indem sie die Quadratwurzel aus negativen Zahlen ermöglichen. Sie bestehen aus einem Realteil und einem Imaginärteil, der mit der Einheit i dargestellt wird, wobei i² = -1 gilt. Dadurch eröffnen sie neue Möglichkeiten für die Mathematik und Physik.")
                        contentOfChapter.add("Die Darstellung komplexer Zahlen erfolgt in der Regel als Summe aus Realteil und Imaginärteil: z = a + bi. Dabei ist a der Realteil und b der Imaginärteil. Diese Zahlen können geometrisch in der sogenannten Gauß’schen Zahlenebene als Punkte oder Vektoren dargestellt werden.")
                        contentOfChapter.add("Rechenoperationen wie Addition, Subtraktion, Multiplikation und Division sind mit komplexen Zahlen möglich. Die Addition erfolgt durch das Addieren der Real- und Imaginärteile, während bei der Multiplikation das Distributivgesetz unter Berücksichtigung von i² = -1 angewandt wird.")
                        contentOfChapter.add("Komplexe Zahlen haben zahlreiche Anwendungen in der Wissenschaft und Technik. In der Elektrotechnik modellieren sie Wechselstromkreise, in der Signalverarbeitung komplexe Wellenformen. Außerdem spielen sie eine Schlüsselrolle in der Quantenmechanik und bei der Lösung von Differentialgleichungen.")
                        question.add(Question(
                            topic = "Komplexe Zahlen",
                            question = "Eine komplexe Zahl wird allgemein z=x+iy geschrieben. Was bedeuten die Größen x und y in dieser Darstellung?",
                            allAnwsers = listOf("x ist der Betrag und y ist das Argument", "x ist der Realteil und y ist der Imaginärteil", "x ist das Argument und y ist der Betrag", "x ist die imaginäre Einheit und y der Realteil"),
                            correctAnwsers = listOf("x ist der Realteil und y ist der Imaginärteil") // Korrekte Antwort hinzugefügt
                        ))
                    }
                    2 -> { // Kapitel 2, 2.1, 2.2, 2.3 etc.
                        contentOfChapter.add("b1")
                        contentOfChapter.add("b2")
                        contentOfChapter.add("b3")
                        contentOfChapter.add("b4")
                        contentOfChapter.add("b5")
                        contentOfChapter.add("b6")
                        contentOfChapter.add("b7")
                        // Frage für Kapitel 2 hinzufügen (Beispiel, nimm die richtige aus deinem C# Code)
                        question.add(Question(
                            topic = "Vektoren",
                            question = "Was bedeutet es für zwei Vektoren u und v (Reellen Zahlen), wenn sie orthogonal zueinander sind?",
                            allAnwsers = listOf("Ihr Skalarprodukt ist null", "Beide Vektoren haben dieselbe Länge", "Ihr Skalarprodukt ist gleich 1", "Sie liegen auf einer gemeinsamen Geraden"),
                            correctAnwsers = listOf("Ihr Skalarprodukt ist null") // Korrekte Antwort hinzugefügt
                        ))
                    }
                    3 -> { // Kapitel 3, 3.1, 3.2, 3.3 etc.
                        contentOfChapter.add("c1")
                        contentOfChapter.add("c2")
                        contentOfChapter.add("c3")
                        contentOfChapter.add("c4")
                        contentOfChapter.add("c5")
                        contentOfChapter.add("c6")
                        contentOfChapter.add("c7")
                        // Frage für Kapitel 3 hinzufügen (Beispiel)
                        question.add(Question(
                            topic = "Matrizen",
                            question = "Betrachten Sie eine 2×2-Matrix A. Was kann man über ihre Invertierbarkeit aussagen, wenn det(A)=0?",
                            allAnwsers = listOf("Die Matrix ist invertierbar", "Die Matrix ist nicht invertierbar", "Die Matrix hat unendlichen Rang", "Die Matrix ist nur invertierbar, wenn sie symmetrisch ist"),
                            correctAnwsers = listOf("Die Matrix ist nicht invertierbar") // Korrekte Antwort hinzugefügt
                        ))
                    }
                }
            }
            "java" -> {
                when (chapterIntPart) {
                    1 -> { // Kapitel 1, 1.1, 1.2, 1.3 etc.
                        contentOfChapter.add("Variablen sind grundlegende Bausteine in Java, die Daten speichern. Jede Variable hat einen Namen, einen Typ und einen Wert. Der Typ definiert die Art der Daten, die gespeichert werden können, wie Zahlen, Zeichenketten oder Wahrheitswerte. Variablen ermöglichen es, Daten während der Programmausführung zu verändern.")
                        contentOfChapter.add("In Java müssen Variablen deklariert werden, bevor sie verwendet werden. Eine Deklaration besteht aus dem Datentyp und dem Namen, zum Beispiel: int zahl; Hier wird eine Variable namens 'zahl' des Typs Integer erstellt. Sie kann später mit einem Wert wie zahl = 10 initialisiert werden.")
                        contentOfChapter.add("Java unterstützt verschiedene Datentypen für Variablen, darunter primitive Typen wie int, double und boolean sowie komplexere Typen wie String und benutzerdefinierte Objekte. Die Wahl des Typs hängt von den Anforderungen der Anwendung ab, z. B. ob ganze Zahlen oder Dezimalzahlen benötigt werden.")
                        contentOfChapter.add("Variablen in Java haben eine Lebensdauer und einen Gültigkeitsbereich (Scope), die bestimmen, wo und wie lange sie verfügbar sind. Lokale Variablen existieren nur innerhalb von Methoden, während Instanzvariablen an ein Objekt gebunden sind und globale Variablen durch die Verwendung des Schlüsselworts static definiert werden.")
                        question.add(Question(
                            topic = "Variablen",
                            question = "Wie deklariert man in Java eine Ganzzahl-Variable mit dem Namen anzahl?",
                            allAnwsers = listOf("int anzahl;", "anzahl = int;", "anzahl int;", "anzahl = 3"),
                            correctAnwsers = listOf("int anzahl;") // Korrekte Antwort hinzugefügt
                        ))
                    }
                    2 -> { // Kapitel 2, 2.1, 2.2, 2.3 etc.
                        contentOfChapter.add("b1")
                        contentOfChapter.add("b2")
                        contentOfChapter.add("b3")
                        contentOfChapter.add("b4")
                        contentOfChapter.add("b5")
                        contentOfChapter.add("b6")
                        contentOfChapter.add("b7")
                        // Frage für Kapitel 2 hinzufügen (Beispiel)
                        question.add(Question(
                            topic = "Schleifen",
                            question = "Welche Schleife in Java führt den Code mindestens einmal aus, bevor die Bedingung erneut geprüft wird?",
                            allAnwsers = listOf("for-Schleife", "while-Schleife", "do-while-Schleife", "if-Abfrage"),
                            correctAnwsers = listOf("do-while-Schleife") // Korrekte Antwort hinzugefügt
                        ))
                    }
                    3 -> { // Kapitel 3, 3.1, 3.2, 3.3 etc.
                        contentOfChapter.add("c1")
                        contentOfChapter.add("c2")
                        contentOfChapter.add("c3")
                        contentOfChapter.add("c4")
                        contentOfChapter.add("c5")
                        contentOfChapter.add("c6")
                        contentOfChapter.add("c7")
                        // Frage für Kapitel 3 hinzufügen (Beispiel)
                        question.add(Question(
                            topic = "OOP-Grundlagen",
                            question = "Was beschreibt eine Klasse in Java?",
                            allAnwsers = listOf("Ein bestimmtes Schlüsselwort im Code", "Eine Vorlage (Bauplan) für Objekte", "Nur statische Konstanten", "Eine zufällige Datensammlung ohne Funktion"),
                            correctAnwsers = listOf("Eine Vorlage (Bauplan) für Objekte") // Korrekte Antwort hinzugefügt
                        ))
                    }
                }
            }
        }
    }
}