package fr.btsciel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Serveur_TCP_Base {
    private static final int PORT = 4000;

    private static final String MESSAGE_ACCUEIL = "Entrez une phrase qui sera mise en majuscule par le serveur (exit pour finir)";

    static void main(String[] args) throws IOException {
        try (ServerSocket serveur = new ServerSocket(PORT)) {
            System.out.println("Serveur en fonctionnement sur le port " + PORT + ".");
            while (true) {
                try {
                    Socket client = serveur.accept();
                    BufferedReader entree = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter sortie = new PrintWriter(client.getOutputStream(), true);
                    System.out.println("Connexion avec : " + client.getInetAddress());
                    sortie.println(MESSAGE_ACCUEIL);
                    String messageRecu;
                    while ((messageRecu = entree.readLine()) != null) {
                        System.out.println("Message reçu : " + messageRecu);
                        if (messageRecu.trim().equalsIgnoreCase("exit")) {
                            sortie.println("JE VOUS DECONNECTE !!!");
                            break;
                        }
                        String reponse = messageRecu.toUpperCase(Locale.ROOT);
                        sortie.println(reponse);
                        System.out.println("Message émis : " + reponse);
                    }
                } catch(IOException e){
                System.err.println("Connexion interrompue : " + e.getMessage());
            }
            System.out.println("Client déconnecté.");
        }
    }
}
}