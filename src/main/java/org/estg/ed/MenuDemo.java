
package org.estg.ed;

import java.util.Scanner;

import org.estg.ed.Enigma.Enigma;
import org.estg.ed.Enigma.EnigmaList;
import org.estg.ed.Enigma.EnigmaLoader;

import Lists.ArrayOrderedList;

/**
 *
 * @author maria
 */
public class MenuDemo {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        String caminhoFicheiro = "C:\\Users\\maria\\TP_ED_8240344_8240334\\src\\main\\java\\org\\estg\\ed\\files_enigmas\\enigmas.json";
        EnigmaList enigmas = EnigmaLoader.loadEnigmasFromJSON(caminhoFicheiro);

        for (Enigma enigma : enigmas) {
            System.out.println("ID: " + enigma.getIdContent());
            System.out.println("Pergunta: " + enigma.getQuestion());
            for(int i = 0; i < enigma.getOptions().length; i++) {
                System.out.println((i) + ": " + enigma.getOptions()[i]);
            }
            System.out.println("Opção Correta: " + enigma.getCorrectAnswer());
            System.out.println("---");

        }
        /*
         * 
         * 
         * System.err.println("Bem vindo ao Labirinto da Glória!");
         * System.err.println("--------------------------------");
         * System.err.println("**** MENU **** ");
         * System.err.println("1 - Jogar");
         * System.err.println("2 - Criar Mapas");
         * System.err.println("3 - Sair");
         * 
         * int opcao = scan.nextInt();
         * 
         * switch (opcao) {
         * case 1 ->
         * System.err.println("Funcionalidade de Jogo ainda não implementada.");
         * case 2 -> System.err.
         * println("Funcionalidade de criação de mapas ainda não implementada.");
         * case 3 -> {
         * System.err.println("Obrigado por jogar! Até à próxima.");
         * return;
         * }
         * default ->
         * System.err.println("Opção inválida. Por favor, escolha uma opção válida.");
         * }
         * 
         */

    }
}
