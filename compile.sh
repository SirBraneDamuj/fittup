#!/bin/sh
find ./src/ -name *.java > sources_list.txt 
javac -d bin @sources_list.txt
rm sources_list.txt
