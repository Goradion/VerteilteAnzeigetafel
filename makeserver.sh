if test -f TafelServer.jar; then
	rm TafelServer.jar
fi

javac tafelServer/TafelServer.java 
jar -cf TafelServer.jar tafelServer/*.class tafelServer/*.form serverRequests/*.class verteilteAnzeigetafel/*.class
jar -ufm TafelServer.jar ServerManifest

