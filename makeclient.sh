if test -f Client.jar; then
	rm Client.jar
fi

javac client/Client.java 
jar -cf Client.jar client/*.class tafelServer/*.class client/*.form serverRequests/*.class verteilteAnzeigetafel/*.class
jar -ufm Client.jar ClientManifest

