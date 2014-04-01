import os
import ftplib

FTP_HOST = os.environ.get('CI_FTP_HOST', 'apps.markdessain.com')
FTP_USER = os.environ.get('CI_FTP_USER', 'm')
FTP_PASS = os.environ.get('CI_FTP_PASS', 'm')

CI_BUILD = os.environ.get('TRAVIS_BUILD_NUMBER', '1')

ftp = ftplib.FTP(FTP_HOST)
ftp.login(FTP_USER, FTP_PASS)

#filename = 'target/scala-2.10/tictactoe-1.0.jar'
filename = 'README.md'

ftp.cwd("/files")
ftp.storlines("STOR deploy-%s.jar" % CI_BUILD, open(filename, 'r'))
