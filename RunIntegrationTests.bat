REM @echo off

call SetClasspath

REM @echo on

cd database
call RestoreDB.bat
cd..

java junit.textui.TestRunner tests.RunIntegrationTests > IntegrationTests.txt
REM java junit.swingui.TestRunner tests.RunIntegrationTests

cd database
call RestoreDB.bat
cd..