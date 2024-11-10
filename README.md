# evoLog_TP2

pdflatex -output-directory rapport/ rapport/rapport.tex 

# Importer le projet avec Eclipse

https://www.microfocus.com/documentation/visual-cobol/vc70/EclWin/GUID-773A19C7-98B2-442D-9D36-240E20E3F2CE.html

# Run

Créer le dossier src/test/resources/ dans evoLog_TP2 et target/img dans littleSpoon.

Dans evoLog_TP2 et littleSpoon run "mvn install".

Pour lancer :

java -cp ./target/TP2-1.0-SNAPSHOT-jar-with-dependencies.jar fr.umontpellier.etu.app.ParserApplication

Pour Créer les graph avec Spoon :

java -cp ./target/littleSpoon-0.0.1-SNAPSHOT-jar-with-dependencies.jar fr.umontpellier.etu.littleSpoon.App
