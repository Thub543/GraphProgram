#!/bin/bash

find . -name "*.class" -type f -delete

javac -cp src/ src/Model/*.java src/Main.java
java -cp src Main

read -p "Press ENTER to exit."
