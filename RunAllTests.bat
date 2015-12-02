REM @echo off

call SetClasspath

cd database
call RestoreDB.bat
cd..

REM @echo on

java junit.textui.TestRunner tests.RunAllTests > AllTests.txt
REM java junit.swingui.TestRunner tests.RunAllTests

cd database
call RestoreDB.bat
cd..