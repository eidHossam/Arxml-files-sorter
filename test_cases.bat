@echo off

REM Compile the Java file
javac -d . ArxmlSorter.java

REM Normal case
echo Testing normal case...
java Lab6.ArxmlSorter Normal.arxml
echo ----------------------------------------------------------------------------------
REM Not valid Autosar file case
echo Testing not valid Autosar file case...
java Lab6.ArxmlSorter WrongExtension.txt
echo ----------------------------------------------------------------------------------
REM Empty file case
echo Testing empty file case...
java Lab6.ArxmlSorter Empty.arxml
echo ----------------------------------------------------------------------------------

REM Clean up compiled Java files
del Lab6\*.class

pause