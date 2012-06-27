NuxLauncher
===========

NuxLauncher is a minecraft launcher developed for the nuxos minecraft server. It supports mods, based on a complete repository system, GUI and console mode, and can launch minecraft !

Installation
------------

    git clone git://github.com/NuxosMinecraft/NuxLauncher.git
    cd NuxLauncher
    mvn package
    java -jar target/NuxLauncher-dev-SNAPSHOT.jar

Repository
------------

The repository is based on yaml files, one remote and a local copy.
For an example of repo, you can see : http://launcher.nuxos-minecraft.fr/repo.yml

Others
------------

You need to apply a specific patch to the minecraft.jar to change the minecraft installation directory.

TODO
------------
- threading
- complete this readme
