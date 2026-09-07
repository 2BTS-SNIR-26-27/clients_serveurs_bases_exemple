package fr.btsciel.udp;

import fr.btsciel.utils.In;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client_UDP_Base {
    private static final int PORT = 5000;
    private static final int TAILLE_MAXIMALE = 512;
    private static final int DELAI_REPONSE_MS = 5000;

    public static void main(String[] args) {
        try {
            InetAddress adresseServeur = InetAddress.getLoopbackAddress();
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(DELAI_REPONSE_MS);
                System.out.println("Client UDP connecté au serveur local sur le port " + PORT + ".");
                System.out.println("Saisissez une phrase (exit pour finir) :");
                while (true) {
                String requete = In.readString();
                if (requete == null) {
                    break;
                }
                byte[] octetsEnvoyes = requete.getBytes(StandardCharsets.UTF_8);
                if (octetsEnvoyes.length > TAILLE_MAXIMALE) {
                    System.out.println("Message trop long : maximum " + TAILLE_MAXIMALE + " octets.");
                    continue;
                }
                envoyerOctets(socket, octetsEnvoyes, adresseServeur, PORT);
                System.out.println("La requête : " + requete);
                try {
                    // Lecture directe du contenu utile du paquet sous forme de bytes.
                    byte[] octetsRecus = recevoirOctets(socket);

                    // Conversion uniquement nécessaire pour afficher le texte.
                    String message = new String(octetsRecus, StandardCharsets.UTF_8);
                    System.out.println("    MESSAGE SERVEUR >  \n      " + message + "\n");
                } catch (SocketTimeoutException e) {
                    System.out.println("Aucune réponse après " + (DELAI_REPONSE_MS / 1000) + " secondes.");
                }
                if (requete.trim().equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println("Saisissez une nouvelle phrase :");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur du client UDP : " + e.getMessage());
        }
    }

    private static void envoyerOctets(DatagramSocket socket, byte[] octets,
                                      InetAddress adresse, int port) throws java.io.IOException {
        DatagramPacket paquet = new DatagramPacket(octets, octets.length, adresse, port);
        socket.send(paquet);
    }

    private static byte[] recevoirOctets(DatagramSocket socket) throws java.io.IOException {
        byte[] buffer = new byte[TAILLE_MAXIMALE];
        DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquet);
        return Arrays.copyOfRange(
                paquet.getData(),
                paquet.getOffset(),
                paquet.getOffset() + paquet.getLength());
    }
}
