* Radio intenet ou Webradio) est une application permettant la diffusion radiophonique des flux audio
par reseaux gr�ce � l'utilisation du Protocole SHoutcast, le seul format de fichier utilis� est mp3
ou ogg.

* la station qui contient les flux audios (streams) se trouve dans le rep�rtoire "C:\Users\User
\Documents\Streams" qui sera cr�er au moment du lansement de l'application.

* l'application contient deux interfaces, une pour le Serveur et l'autre pour la Station ainsi que la manipulation
de des streams...

* Pour Lancer le programme il faut tout d'abord :
	1- Avant de demmarer le serveur ajouter � la station 'le dossier Streams' (ce dossier va etre cr�er
	   automatiquement une fois vous lancez le .jar) 

	2- Apr�s vous pouvez lancer le serveur en cliquant sur start

	3- Une fois le seveur d�marrera vous pouvez faire des manipulations sur la station :
	    - Ajouter un stream localement
	    - Ajouter un stream � partir un serveur distant (ex.http://streaming.radio.funradio.fr/fun-1-48-192)
	    - Supprimer un stream
	    - Connecter au serveur localement � partir d'un m�dia player (Winamp Player est demander https://www.winamp.com/)
	       et lancer tous les flux 
	    - Deplacer les flux sur la station

Pour tester avec un client WinAMP vous pouvez soit:
 
* Tester Localement

      - D�marrer le Serveur => Ouvrir Winmap => Cliquer Add (sur Winamp PlayList Editor) => Add URL => Entrer 
       l'adresse local du serveur avec le port utilis� qui devrait appara�tre dans la console du serveur 
       une fois se d�rnier a �t� lancer lancer: "http://localhost:port"
      - Clicker sur le boutton 'Connect to stream' dans la fen�tre station - n'oubliez pas de d�finire Winamp
        comme programme par d�faut pour les fichiers avec l'extention .pls

* Tester le Client � Distance (Plusieurs Clients = Plusieurs Machines)

      Plusieur Clients peuvent se connecter au serveur/station au m�me temps, la procedure est la suivante:
    
	  - Lancer Winamp et ajouter une playlist avec l'url et le port de la machine sur laquelle le serveur est lanc�
	    ensuit lan	
	  - Sur Winamp vous pouvez tester plusieurs Clients sur la m�me machine en modifiant les
	    parametres du logiciel dans Options=>Preferences(ctrl+P)=>General Preferences=>Cocher Allow multiple 
	    instances, comme ca vous pouvez ouvrir plusieurs instances de Winamp et tester la diffusion
	    dans chacune d'elle
		  
	  - La lecture du flux stream est synchronis�e entre tous les clients connect�e au serveur
		 
		  - Au niveau du Log (Server Panel) Vous Trouvrez le nom et l'@ du chaque nouveau client...  
		  
		  - Une fois un fichier du stream est achev� le suivant se lance automatiquement

 
