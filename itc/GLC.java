/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package itc;

import java.util.Arrays;

/**
 *
 * @author Sarah Carolina, Raphaella Aquino, Luciana Guimaraes, Carlos Paixao
 */
public class GLC {
    
    private char Prox; //O Proximo Terminal
    private String Palavra; //A palavra, sentenção, codigo fonte, etc.
    private int Pos; //Posição atual na palavra/sentença
    private int Erros; //Quantidade de erros encontrados
    private char[] Alfabeto = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','w','y','z'};
    private char[] Numeros = {'0','1','2','3','4','5','6','7','8','9'};
    
    private char obterProx(){
        char Lido;
        while(Pos < Palavra.length()){  //Se não terminou a palavra
            Lido = Palavra.charAt(Pos++);
            if(!Character.isWhitespace(Lido)){ //ignora espaços brancos
                return Lido;
            }
        }
        return 0; //char de código zero, só ocorre quando terminou a palavra
    }
    
    private void casaTerminal(char Esperado){
        if(Prox == Esperado){
            System.out.println("    casaTerminal("+Prox+")");
            Prox = obterProx();
        }
        else{
            Erros++;
            System.out.println("erro. Esperado: "+Esperado+" Recebido: "+Prox);
        }
    }
    
    private void casaTerminal(char[] Esperado){
		int index = Arrays.binarySearch(Esperado, Prox);
        if(index >= 0){
            System.out.println("    casaTerminal("+Prox+")");
            Prox = obterProx();
        }
        else{
            Erros++;
            int cont = 0;
            String arr = "(";
            while (cont < Esperado.length) {
            	arr = arr.concat(Esperado[cont]+" | ");
            	cont++;
            }
            arr = arr.substring(0, (arr.length()-3));
            arr = arr.concat(")");
            
            System.out.println("erro. Esperado: "+arr+" Recebido: "+Prox);
        }
    }
    
    private boolean reconhecer(String Palavra){
        //Inicializa os campos da classe
        this.Palavra = Palavra;
        this.Pos = 0;
        this.Erros = 0;
        this.Prox = obterProx(); //Inicia a leitura dos Terminais
        Begin(); //Símbolo Inicial da Gramática
        
        if(Prox!=0){ //A gramática terminou mas a palavra não
            Erros++;
            System.out.println("erro. A gramática terminou mas a palavra não.");
        }
        
        return (Erros == 0); //Se não houve erros, então reconheceu a palavra
    }

    private void Begin() {
    	casaTerminal('b');
    	casaTerminal('e');
    	casaTerminal('g');
    	casaTerminal('i');
    	casaTerminal('n');
    	Comandos();
    	end();
    }
    
    private void Comandos(){
        if (Prox=='s') {
        	casaTerminal('s');
        	casaTerminal('e');
        	casaTerminal('t');
        	set();
        	Comandos();
        } else if (Prox=='m') {
        	casaTerminal('m');
        	casaTerminal('e');
        	casaTerminal('m');
        	mem();
        	Comandos();
        } else if (Prox=='r') {
        	casaTerminal('r');
        	casaTerminal('e');
        	casaTerminal('t');
        	ret();
        	Comandos();
        } else if (Prox=='o') {
        	casaTerminal('o');
        	casaTerminal('u');
        	casaTerminal('t');
        	out();
        	Comandos();
        } else if (Prox=='a') {
        	casaTerminal('a');
        	casaTerminal('s');
        	casaTerminal('m');
        	asm();
        	Comandos();	
        }
    }
    
    private void separator() {
    	if (Prox=='<') {
    		casaTerminal('<');
    		casaTerminal('<');
    	}
    }
    
    private void end() {
    	casaTerminal('e');
        casaTerminal('n');
        casaTerminal('d'); 
        casaTerminal(';');
    }
    
    private void set(){
    	separator();
		casaTerminal('&');
		casaTerminal(Alfabeto);
    	separator();
	
        if (Prox=='&') {
    		casaTerminal('&');
    		casaTerminal(Alfabeto);
    		casaTerminal(';');
        } else if (Prox=='#') {
    		casaTerminal('#');
    		casaTerminal(Numeros);
	        while(Character.isDigit(Prox)) {
        		casaTerminal(Numeros);
	        }
    		casaTerminal(';');
        }	
    }
    
    private void mem(){
    	separator();
		casaTerminal('#');
		casaTerminal(Numeros);
        while(Character.isDigit(Prox)) {
    		casaTerminal(Numeros);
        }
        
    	separator();
		casaTerminal('&');    
		casaTerminal(Alfabeto);
		casaTerminal(';');
    }

    private void ret(){
    	separator();
		casaTerminal('&');    
		casaTerminal(Alfabeto);
		casaTerminal(';');
    }
    
    private void out() {
    	separator();
    	casaTerminal('&');
    	casaTerminal(Alfabeto);
    	while(Prox=='<') {
	    	separator();
			casaTerminal('&');    
			casaTerminal(Alfabeto);
		}
		casaTerminal(';');
    }
    
    private void asm() {
    	separator();
    	casaTerminal('#');
    	casaTerminal(Numeros);
    	while(Prox=='<') {
	    	separator();
			casaTerminal('&');    
			casaTerminal(Alfabeto);
		}
		casaTerminal(';');
    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GLC glc = new GLC();
        System.out.println("========================================");
        System.out.printf("Pertence a gramática? %b \n", glc.reconhecer("begin end ;"));

        System.out.println("========================================");
        System.out.printf("Pertence a gramática? %b \n", glc.reconhecer("begin mem << #123 << &a ; set << &b << #101 ; asm << #1 << &c << &a << &b ; ret << &d ; out << &c ; end;"));

    }
    
}
