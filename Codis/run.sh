#!/bin/bash

#Remove the sources.txt fuke if it exists
rm -rf sources.txt

#Remove the builds folder if it exists
rm -rf build

#Re-generate the sources.txt file
cd src/Exercici2; find -name "*.java" > ../../sources.txt

#Create the build directory
mkdir ../../build

#Compile project
javac @../../sources.txt -d ../../build

cd ../../

#Remove the sources.txt file
rm -rf sources.txt

#Launch the program
cd build; java Exercici2/Main;cd ../; rm -rf build




