from fabric.api import env
from fabric.operations import run, local, put
from fabric.context_managers import shell_env

env.hosts = ['root@82.196.3.173']

def assembly():
    local('sbt assembly')

def copy(scala_version='2.10', app_name='TicTacToe', app_version='1.0'):
    run('mkdir -p /home')
    put('target/scala-%(scala_version)s/%(app_name)s-assembly-%(app_version)s.jar' % locals(), '/home')

def start(app_name='TicTacToe', app_version='1.0'):
    with shell_env(PORT='5000'):
        run('java -jar /home/%(app_name)s-assembly-%(app_version)s.jar' % locals())
