@echo off
del /S /Q *.class > nul 2>&1

javac -cp src/ src/Model/*.java src/Main.java

java -cp src Main
echo Press ENTER to exit.
pause
