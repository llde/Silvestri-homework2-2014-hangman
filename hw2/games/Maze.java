package hw2.games;

import hw2.game.Action;
import hw2.game.Param;
import hw2.game.WGState;
import hw2.game.WGame;
import hw2.game.util.SimpleParam;
import hw2.game.util.SimpleWGState;
import hw2.game.util.Utils;

import java.util.*;

public class Maze implements WGame {
    private List<Param<?>> lst_param = new ArrayList<Param<?>>();
    private String str_parola = "";
    private String str_mancanti = "";
    private String str_indovina = "";
    private String str_game = "";
    private int posx = -1;
    private int posy = -1;
    private SimpleWGState stato;
    private String num_lives = null;
    private int dist_view = -1;
    private int num_steps = -1;
    private char[][] map_origin = {
        {'X','X','X','X','X','X','X','X','X','X'},
        {'X','O','X',' ',' ',' ',' ',' ',' ','X'},
        {'X',' ','X','X','X','F',' ','X',' ','X'},
        {'X',' ','X','X','X','X','X','X',' ','X'},
        {'X',' ',' ',' ','X',' ',' ',' ',' ','X'},
        {'X',' ','X',' ',' ',' ','X','X','X','X'},
        {'X',' ','X','X',' ','X','X',' ',' ','#'},
        {'X',' ','X',' ',' ',' ','X',' ','X','X'},
        {'X',' ',' ',' ','X',' ',' ',' ',' ','X'},
        {'X','X','X','X','X','X','X','X','X','X'},
    };
    private char[][] map = {
            {'X','X','X','X','X','X','X','X','X','X'},
            {'X','O','X',' ',' ',' ',' ',' ',' ','X'},
            {'X',' ','X','X','X','F',' ','X',' ','X'},
            {'X',' ','X','X','X','X','X','X',' ','X'},
            {'X',' ',' ',' ','X',' ',' ',' ',' ','X'},
            {'X',' ','X',' ',' ',' ','X','X','X','X'},
            {'X',' ','X','X',' ','X','X',' ',' ','#'},
            {'X',' ','X',' ',' ',' ','X',' ','X','X'},
            {'X',' ',' ',' ','X',' ',' ',' ',' ','X'},
            {'X','X','X','X','X','X','X','X','X','X'},
    };

    public Maze() {
        //crea i parametri
        lst_param.add(new SimpleParam("Numero di vite", 0, "uno", "due", "tre"));
        lst_param.add(new SimpleParam("Distanza visuale", 3, 1, 2, 3, 10));
        lst_param.add(new SimpleParam("Numero passi", 0, 15, 20, 25, 30, 35, 40, 45, 50));
        stato = new SimpleWGState(Action.END, "");
    }

    /** @return il nome del gioco, cioè "MAZE" */
    @Override
    public String name() {
        String str = "MAZE";
        return str;
    }

    /** @return le informazioni sul gioco, cioè
    <pre>
    Trova l'uscita senza morire
    </pre> */
    @Override
    public String info() {
        String str = "Raggiungi l'uscita.\n" +
                "Tu sei 'O', le mura sono 'X'\n" +
                "'#' è una porta mentre 'F' è la chiave";
        return str;
    }

    /** Ritorna la lista dei parametri che per Maze è un parametro
     * con le seguenti caratteristiche
     <pre>
     prompt: "Numero di vite"
     values: [1,2,3]
     valore di default: 1
     prompt: "Distanza visuale"
     values: [1,2,3]
     valore di default: 1

     </pre>
     * @return la lista dei parametri */
    @Override
    public List<Param<?>> params() {
        return lst_param;
    }

    /** Inizia un nuovo gioco con le vite specificate dal parametro */
    @Override
    public WGState newGame() {
        if(stato.action().compareTo(Action.CONTINUE) == 0)
            throw new IllegalArgumentException();

        posx = 1;
        posy = 1;

        num_lives = (String) lst_param.get(0).get();
        dist_view = (Integer) lst_param.get(1).get();
        num_steps = (Integer) lst_param.get(2).get();
        str_game = name()+"\n"+info()+"\n";

        //ripristino la struttura iniziale del livello
        map = map_origin;
        //modifica str_game aggiungedo i caratteri della mappa
        remap();

        //classico
        /*for(int x=0; x<map.length; x++){
            for(int y=0; y<map[0].length; y++){
                str_game += map[x][y];//+" ";
            }
            str_game += "\n";
        }*/

        //aggiorno lo stato
        stato = new SimpleWGState(Action.CONTINUE, str_game);

        return stato;
    }

    /** Accetta la stringa s del giocatore. Prima di tutto riduce s in minuscolo.
     * @param s  la mossa del giocatore
     * @return il nuovo stato di gioco
     * @throws IllegalStateException se non c'è un gioco attivo */
    @Override
    public WGState player(String s) {
        if(stato.action().compareTo(Action.CONTINUE) != 0)
            throw new IllegalStateException();

        String str_input = s.toLowerCase();
        if (!Utils.isAlphaLowercase(str_input) || str_input.length() > 1)
            return stato;

        boolean game_over = false;
        boolean exit = false;
        str_game = "";

        //posx e posy sono invertiti MDD
        switch(str_input.charAt(0)){
            case 'w':
                exit = checkExit(posy -1, posx);
                if(exit)
                    break;
                if(map[posy -1][posx] != 'X'){
                    if(map[posy -1][posx] != '#'){
                        map[posy][posx] = ' ';
                        posy -= 1;
                        num_steps -= 1;
                        map[posy][posx] = 'O';
                        remap();
                    }
                    else{
                        remap();
                        str_game += "La porta è chiusa"+"\n";
                    }
                }
                checkKeyDoor(posy - 1, posx);

                break;
            case 's':
                exit = checkExit(posy -1, posx);
                if(exit)
                    break;
                if(map[posy +1][posx] != 'X'){
                    if(map[posy +1][posx] != '#'){
                        map[posy][posx] = ' ';
                        posy += 1;
                        num_steps -= 1;
                        map[posy][posx] = 'O';
                        remap();
                    }
                    else{
                        remap();
                        str_game += "La porta è chiusa"+"\n";
                    }
                }
                checkKeyDoor(posy + 1, posx);

                break;
            case 'a':
                exit = checkExit(posy -1, posx);
                if(exit)
                    break;
                if(map[posy][posx -1] != 'X'){
                    if(map[posy][posx -1] != '#'){
                        map[posy][posx] = ' ';
                        posx -= 1;
                        num_steps -= 1;
                        map[posy][posx] = 'O';
                        remap();
                    }
                    else{
                        remap();
                        str_game += "La porta è chiusa"+"\n";
                    }
                }
                checkKeyDoor(posy, posx - 1);

                break;
            case 'd':
                exit = checkExit(posy -1, posx);
                if(exit)
                    break;
                if(map[posy][posx +1] != 'X'){
                    if(map[posy][posx +1] != '#'){
                        map[posy][posx] = ' ';
                        posx += 1;
                        num_steps -= 1;
                        map[posy][posx] = 'O';
                        remap();
                    }
                    else{
                        remap();
                        str_game += "La porta è chiusa"+"\n";
                    }
                }
                checkKeyDoor(posy, posx + 1);

                break;
            default:
                remap();
                str_game += "Premi w per muoverti in alto;"+"\n";
                str_game += "Premi s per muoverti in basso;"+"\n";
                str_game += "Premi a per muoverti a sinistra;"+"\n";
                str_game += "Premi d per muoverti a destra."+"\n";
                break;
        }

        if(num_steps == 0){
            game_over = true;
        }

        if(!game_over){
            if(exit){
                str_game += "Sei riuscito a fuggire!"+"\n";
                stato = new SimpleWGState(Action.END, str_game);
            }
            else{
                stato = new SimpleWGState(Action.CONTINUE, str_game);
            }
        }
        else{
            str_game += "La stanchezza ti ha sopraffatto, hai perso."+"\n";
            stato = new SimpleWGState(Action.END, str_game);
        }

        return stato;
    }

    /** Termina prematuramente l'eventuale gioco attivo */
    @Override
    public void abort() {
        stato = new SimpleWGState(Action.END, str_game);
    }

    private void remap(){
        if(dist_view == 10){
            //classico
            for(int x=0; x<map.length; x++){
                for(int y=0; y<map[0].length; y++){
                    str_game += map[x][y];//+" ";
                }
                str_game += "\n";
            }
        }
        else {
            int ics = 0;
            int ips = 0;
            int min_ics = 0; //0
            int min_ips = 0; //0
            int max_ics = map.length - 1; //0
            int max_ips = map[0].length - 1; //0

            if (posx - dist_view >= 0)
                min_ics = posx - dist_view;
            if (posy - dist_view >= 0)
                min_ips = posy - dist_view;
            if (posy + dist_view < map[0].length)
                max_ips = posy + dist_view;
            if (posx + dist_view < map.length)
                max_ics = posx + dist_view;

            ics = min_ics;
            ips = min_ips;

            str_game += "---------------" + "\n";
            while (ips <= max_ips) {
                while (ics <= max_ics) {
                    str_game += map[ips][ics];
                    ics++;
                }
                str_game += "\n";
                ics = min_ics;
                ips++;
            }
            str_game += "---------------" + "\n";
        }
    }

    private void checkKeyDoor(int ips, int ics){
        if(map[ips][ics] == 'F'){
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    if(map[y][x] == '#')
                        map[y][x] = '|';
                }
            }
        }
    }

    private boolean checkExit(int ips, int ics){
        if(map[ips][ics] == '|'){
            return true;
        }
        return false;
    }

}
