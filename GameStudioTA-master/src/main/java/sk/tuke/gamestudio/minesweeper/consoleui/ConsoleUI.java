package sk.tuke.gamestudio.minesweeper.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.minesweeper.core.Field;
import sk.tuke.gamestudio.minesweeper.core.GameState;
import sk.tuke.gamestudio.minesweeper.core.Tile;
import sk.tuke.gamestudio.service.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Console user interface.
 */
@Transactional
public class ConsoleUI implements UserInterface {
    /**
     * Playing field.
     */
    private Field field;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private OccupationService occupationService;

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Input reader.
     */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private Settings setting;

    /**
     * Reads line of text from the reader.
     *
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void newGameStarted(Field field) {
        String player = handleName();
        int gameScore = 0;
        this.field = field;
        System.out.println("Pick your difficulty:");
        System.out.println("(1) BEGINNER, (2) INTERMEDIATE, (3) EXPERT, (ENTER) leave DEFAULT");
        String level = readLine();
        if (level != null && !level.equals("")) {
            try {
                int intLevel = Integer.parseInt(level);
                Settings s = switch (intLevel) {
                    case 2 -> Settings.INTERMEDIATE;
                    case 3 -> Settings.EXPERT;
                    default -> Settings.BEGINNER;
                };
                this.setting = s;
                this.setting.save();
                this.field = new Field(s.getRowCount(), s.getColumnCount(), s.getMineCount());
            } catch (NumberFormatException e) {
            }
        }
        try {
            do {
                update();
                processInput();
                var fieldState = this.field.getState();

                if (fieldState == GameState.FAILED) {
                    System.out.println("Game over, potato. Your score is " + gameScore + ".");
                    handleRating(player);
                    handleComment(player);
                    break;
                } else if (field.getState() == GameState.SOLVED) {
                    gameScore = this.field.getScore();
                    scoreService.addScore(new Score("minesweeper", player, field.getScore(), new Date()));
                    System.out.printf("Glorious victory! Your score is " + gameScore + "\n");
                    handleRating(player);
                    handleComment(player);
                    System.exit(0);
                }
            } while (true);
            System.exit(0);
        } catch (GameStudioException e) {
            System.out.println("Cannot access database. (\"+e.getMessage()+\")");
        }
    }

    @Override
    public void update() {
        System.out.print("   ");
        for (int i = 0; i < field.getColumnCount(); i++) {
            System.out.printf("%3s", i);
        }
        System.out.println();
        for (int i = 0; i < field.getRowCount(); i++) {
            System.out.printf("%3c", (i + 65));
            for (int j = 0; j < field.getColumnCount(); j++) {
                Tile t = field.getTile(i, j);
                if (t.getState() == Tile.State.OPEN) {
                    System.out.printf("%3s", t);
                }
                if (t.getState() == Tile.State.MARKED) {
                    System.out.printf("%3s", "M");
                }
                if (t.getState() == Tile.State.CLOSED) {
                    System.out.printf("%3s", "-");
                }
            }
            System.out.println();
        }
        field.getRemainingMineCount();
        System.out.printf("Remaining number of marked/remaining mines: %d%n", field.getRemainingMineCount());
        System.out.println("Please enter your selection <X> EXIT, <MA1> MARK, <OB4> OPEN: ");
    }

    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
    private void processInput() {
        String line = Objects.requireNonNull(readLine()).trim().toUpperCase();
        try {
            handleInput(line);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleInput(String input) throws WrongFormatException {
        if (input.equals("X")) {
            System.out.println("Game exited.");
            System.exit(1);
        }
        Pattern pattern = Pattern.compile("(O|M)([A-P])([\\d+])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input.toUpperCase());

        if (!matcher.matches()) {
            throw new WrongFormatException("Wrong input.");
        }
        int row = matcher.group(2).charAt(0) - 65;
        int column = Integer.parseInt(matcher.group(3));
        if (matcher.group(1).equals("O")) {
            field.openTile(row, column);
        } else {
            field.markTile(row, column);
        }
    }

    private void handleRating(String playerName) throws GameStudioException {
        int inputRating;
        do {
            System.out.println("Rating expected to be between 1-5.");
            System.out.println("Add rating: ");
            inputRating = Integer.parseInt(Objects.requireNonNull(readLine()));
        } while (inputRating > 5 || inputRating < 1);
        ratingService.setRating(new Rating("minesweeper", playerName, inputRating, new Date()));
        System.out.printf("Average Rating:%S\n", ratingService.getAverageRating("minesweeper"));
    }

    private void handleComment(String playerName) throws GameStudioException {
        String comment;
        do {
            System.out.println("Comment length expected to be between 1-1000 characters.");
            System.out.println("Add comment: ");
            comment = readLine();
        } while (Objects.requireNonNull(comment).length() > 1000);
        commentService.addComment(new Comment("minesweeper", playerName, comment, new Date()));
        for (Comment c : commentService.getComments("minesweeper")) {
            System.out.printf("Commented on %s by %s: %s \n", c.getCommentedOn(), c.getUsername(), c.getComment());
        }
    }

    public String handleName() {
        String username;
        do {
            System.out.println("Username length expected to be between 1-32 characters.");
            System.out.println("Enter username: ");
            username = readLine();
        } while (Objects.requireNonNull(username).length() > 32 || username.length() < 1);
        try {
            List<Player> playerList = playerService.getPlayersByUserName(username);
            System.out.println(playerList);
            if (playerList.size() != 0) {
                for (Player player : playerList) {
                    System.out.println(player.toString());
                }
            } else handleNewUser(username);
        } catch (GameStudioException e) {
            System.out.println("Unable to access database. (" + e.getMessage() + ")");
        }
        return username;
    }

    public void handleNewUser(String username) {
        try {
            System.out.println("Enter your fullname: ");
            String fullname = readLine();
            System.out.println("Enter selfEvaluation: ");
            int evaluation = Integer.parseInt(Objects.requireNonNull(readLine()));
            System.out.println("Add Country: ");
            Country country = inputCountry();
            System.out.println("Add Occupation <ziak>, <student>, <zamestnanec>, <zivnostnik>, <nezamestnany>, <dochodca>, <invalid>");
            Occupation occupation = inputOccupation();
            playerService.addPlayer(new Player(username, fullname, evaluation, country, occupation));
        } catch (GameStudioException e) {
            System.out.println("Unable to access database. (" + e.getMessage() + ")");
        }
    }

    public Country inputCountry() {
        System.out.println("Listing Countries: ");
        List<Country> countries = countryService.getCountries();
        System.out.println(countries);
        System.out.println("Pick your country number or enter <0> for New Country");
        int input = Integer.parseInt(Objects.requireNonNull(readLine()));
        if (input == 0) {
            System.out.println("Enter New Country: ");
            countryService.addCountry(new Country(readLine()));
        }
        return countryService.getCountries().get(input);
    }

    public Occupation inputOccupation() {
        System.out.println("Listing Occupations: ");
        List<Occupation> occupations = occupationService.getOccupations();
        System.out.println(occupations);
        System.out.println("Pick your occupation number.");
        int input = Integer.parseInt(Objects.requireNonNull(readLine()));
        return occupationService.getOccupations().get(input);
    }
//    public String handleName() throws GameStudioException{
//        String playerName;
//        do {
//            System.out.println("Name length expected to be between 1-25 characters.");
//            System.out.println("What should I call you: ");
//            playerName = readLine();
//        } while (Objects.requireNonNull(playerName).length() > 25 || playerName.length() < 1);
//        return playerName;
//    }

    @Override
    public void play() {
        setting = Settings.load();

        Field field = new Field(
                setting.getRowCount(),
                setting.getColumnCount(),
                setting.getMineCount()
        );
        newGameStarted(field);
    }
}