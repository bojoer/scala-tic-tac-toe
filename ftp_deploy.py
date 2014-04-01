import os
import glob
import ftplib

FTP_HOST = os.environ.get('FTP_HOST')
FTP_USER = os.environ.get('FTP_USER')
FTP_PASS = os.environ.get('FTP_PASS')

CI_BUILD = os.environ.get('TRAVIS_BUILD_NUMBER')

ftp = ftplib.FTP(FTP_HOST)
ftp.login(FTP_USER, FTP_PASS)

release_directory = "/files/%s" % CI_BUILD

try:
    ftp.delete(release_directory)
    print "Deleted directory: %s" % release_directory
except ftplib.error_perm:
    print "Unable to delete directory: %s" % release_directory

try:
    ftp.mkd(release_directory)
    print "Made directory: %s" % release_directory
except ftplib.error_perm:
    print "Unable to make directory: %s" % release_directory

ftp.cwd("/files/%s" % CI_BUILD)
file_paths = glob.glob('target/scala-*/*.jar')

for file_path in file_paths:

    file_name = file_path.split("/")[-1]

    ftp.storlines("STOR %s" % file_name, open(file_path, 'r'))
    print "Written file: %s" % file_path
