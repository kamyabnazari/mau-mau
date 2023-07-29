package htw.berlin.de.ui.runner;

import htw.berlin.de.game_manager.entity.Game;
import htw.berlin.de.game_manager.export.GameService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {

    private final GameService gameService;
    private final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    public MyRunner(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner input = new Scanner(System.in);
        boolean running = true;
        do {
            System.out.println("Do you want to continue the game with a given id? : Yes/No");
            String choice = input.nextLine();
            switch(choice){
                case "YES", "Y", "Yes", "y", "yes"-> {
                    System.out.println("Please input the id of the game you want to continue: ");
                    long id = input.nextLong();
                    if(gameService.getGameById(id)!=null) {
                        if(gameService.getGameById(id).isRunning()) {
                            renderBoard();
                        }
                        else{
                            System.out.println(String.format("the game with id %d has already finished, please enter another id or hit New Game", id));
                        }
                    }
                    else {
                        System.out.println(String.format("the game with id %d does not exist, please enter another id or hit New Game", id));
                    }
                }
                default -> {
                    System.out.println("Starting new game");
                    System.out.println("----------------------------");
                    System.out.println("Please input your name: ");
                    String name = input.nextLine();
                    Game game = gameService.startGame(name);
                }
            }

        } while (running);
        System.out.println("Goodbye!..");
    }

    public String renderBoard(){
        return "";
    }
}