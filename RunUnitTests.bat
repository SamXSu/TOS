REM @echo off

call SetClasspath

REM @echo on

cd database
call RestoreDB.bat
cd..

java junit.textui.TestRunner tests.RunUnitTests > UnitTests.txt
REM java junit.swingui.TestRunner tests.RunUnitTests

cd database
call RestoreDB.bat
cd..