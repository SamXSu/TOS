REM @echo off

REM
REM This script requires that the "javac" command be on the system command path.
REM This can be accomplished by modifying the path statement below
REM to include "C:\Program Files\Java\jdk1.6.0_03\bin" (or whatever your path is).
REM Alternatively, you could add the path to "javac" to the PATH Environment variable: 
REM   Settings-->Control Panel-->System-->Advanced-->Environment Variables-->Path
REM


REM clean all .class files out of the bin directory
cd bin
erase /S /Q *.class
cd ..

call SetClassPath

REM @echo on

javac -d bin\ -cp %classpath% src\tos\objects\*.java src\tos\persistence\*.java src\tos\application\*.java src\tos\business\*.java src\tos\presentation\*.java
javac -d bin\ -cp %classpath% src\tests\objects\*.java src\tests\persistence\*.java src\tests\application\*.java src\tests\business\*.java src\tests\*.java
