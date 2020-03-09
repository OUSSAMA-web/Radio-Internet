* Radio intenet ou Webradio) est une application permettant la diffusion radiophonique des flux audio
par reseaux grâce à l'utilisation du Protocole SHoutcast, le seul format de fichier utilisé est mp3
ou ogg.

* la station qui contient les flux audios (streams) se trouve dans le repértoire "C:\Users\User
\Documents\Streams" qui sera créer au moment du lansement de l'application.

* l'application contient deux interfaces, une pour le Serveur et l'autre pour la Station ainsi que la manipulation
de des streams...

* Pour Lancer le programme il faut tout d'abord :
	1- Avant de demmarer le serveur ajouter à la station 'le dossier Streams' (ce dossier va etre créer
	   automatiquement une fois vous lancez le .jar) 

	2- Après vous pouvez lancer le serveur en cliquant sur start

	3- Une fois le seveur démarrera vous pouvez faire des manipulations sur la station :
	    - Ajouter un stream localement
	    - Ajouter un stream à partir un serveur distant (ex.http://streaming.radio.funradio.fr/fun-1-48-192)
	    - Supprimer un stream
	    - Connecter au serveur localement à partir d'un média player (Winamp Player est demander https://www.winamp.com/)
	       et lancer tous les flux 
	    - Deplacer les flux sur la station

Pour tester avec un client WinAMP vous pouvez soit:
 
* Tester Localement

      - Démarrer le Serveur => Ouvrir Winmap => Cliquer Add (sur Winamp PlayList Editor) => Add URL => Entrer 
       l'adresse local du serveur avec le port utilisé qui devrait apparaître dans la console du serveur 
       une fois se dérnier a été lancer lancer: "http://localhost:port"
      - Clicker sur le boutton 'Connect to stream' dans la fenêtre station - n'oubliez pas de définire Winamp
        comme programme par défaut pour les fichiers avec l'extention .pls

* Tester le Client à Distance (Plusieurs Clients = Plusieurs Machines)

      Plusieur Clients peuvent se connecter au serveur/station au même temps, la procedure est la suivante:
    
	  - Lancer Winamp et ajouter une playlist avec l'url et le port de la machine sur laquelle le serveur est lancé
	    ensuit lan	
	  - Sur Winamp vous pouvez tester plusieurs Clients sur la même machine en modifiant les
	    parametres du logiciel dans Options=>Preferences(ctrl+P)=>General Preferences=>Cocher Allow multiple 
	    instances, comme ca vous pouvez ouvrir plusieurs instances de Winamp et tester la diffusion
	    dans chacune d'elle
		  
	  - La lecture du flux stream est synchronisée entre tous les clients connectée au serveur
		 
		  - Au niveau du Log (Server Panel) Vous Trouvrez le nom et l'@ du chaque nouveau client...  
		  
		  - Une fois un fichier du stream est achevé le suivant se lance automatiquement

 
